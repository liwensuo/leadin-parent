package org.leadin.portal.controller;

import org.leadin.common.bean.BaseBean;
import org.leadin.common.bean.ClientInfoBean;
import org.leadin.common.constant.Constants;
import org.leadin.common.util.LogUtil;
import org.leadin.common.util.StringUtil;
import org.leadin.portal.common.page.Pagination;
import org.leadin.portal.service.IExport;
import org.leadin.portal.util.ConfigUtil;
import org.leadin.portal.util.DateUtil;
import org.leadin.portal.util.Tools;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * [leadin.export.controller]
 *
 * @ProjectName: [leadin]
 * @Author: [Jon.K]
 * @CreateDate: [2015/2/28 10:34]
 * @Update: [说明本次修改内容] BY[Jon][2015/2/28 10:34]
 * @Version: [v1.0]
 */
@Controller
@RequestMapping(value = "/")
public class BaselogController {
    @Autowired
    public IExport baseLogExportImpl;
    @Autowired
    public IExport loginLogExportImpl;
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 首页日志查询页面
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "log/toSearch.do", method = RequestMethod.POST)
    public ModelAndView logList(HttpServletRequest request, Model model) {
        return new ModelAndView("log_list").addObject("key", request.getParameter("key")).addObject("tablename", request.getParameter("tablename")).addObject("startTime", request.getParameter("startTime")).addObject("endTime", request.getParameter("endTime"));
    }

    @RequestMapping(value = "baseLog/search.do")
    public ModelAndView searchIndex(HttpServletRequest request, Model model) {
        return new ModelAndView("log_search").addObject("names", getClientTableMap());
    }

    /**
     * 异步分页查询
     *
     * @param key
     * @param tablename
     * @param request
     * @param response
     * @throws java.io.IOException
     */
    @RequestMapping(value = "log/search.do")
    public void datatablesPage(@RequestParam(value = "iDisplayStart") int iDisplayStart, @RequestParam(value = "iDisplayLength") int pageSize, @RequestParam(value = "key") String key, @RequestParam(value = "startTime") String startTime, @RequestParam(value = "endTime") String endTime, @RequestParam(value = "tablename") String tablename, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        PrintWriter printWriter = response.getWriter();

        Criteria criteria = new Criteria();
        List<Criteria> criterias = new ArrayList<Criteria>();
        if (!StringUtil.isBlank(key)) {
            //模糊匹配，忽略大小写
            Pattern pattern = Pattern.compile("^.*" + Tools.escapeStr(key.trim()) + ".*$", Pattern.CASE_INSENSITIVE);
            //或查询 匹配所有字段
            criterias.add(Criteria.where("content").regex(pattern));
            criterias.add(Criteria.where("tomcatName").regex(pattern));
            criterias.add(Criteria.where("serverIp").regex(pattern));
            criterias.add(Criteria.where("id").is(pattern));
            Criteria[] criteria2 = new Criteria[criterias.size()];
            criteria.orOperator(criterias.toArray(criteria2));
            if (!StringUtil.isBlank(startTime) && !StringUtil.isBlank(endTime)) {
                criteria.andOperator(Criteria.where("time").gte(DateUtil.reverse2Date(startTime)).lte(DateUtil.reverse2Date(endTime)));
            } else if (!StringUtil.isBlank(startTime)) {
                criteria.andOperator(Criteria.where("time").gte(DateUtil.reverse2Date(startTime)));
            } else if (!StringUtil.isBlank(endTime)) {
                criteria.andOperator(Criteria.where("time").lte(DateUtil.reverse2Date(endTime)));
            }
        } else {
            if (!StringUtil.isBlank(startTime) && !StringUtil.isBlank(endTime)) {
                criteria.andOperator(Criteria.where("time").gte(DateUtil.reverse2Date(startTime)).lte(DateUtil.reverse2Date(endTime)));
            } else if (!StringUtil.isBlank(startTime)) {
                criteria.andOperator(Criteria.where("time").gte(DateUtil.reverse2Date(startTime)));
            } else if (!StringUtil.isBlank(endTime)) {
                criteria.andOperator(Criteria.where("time").lte(DateUtil.reverse2Date(endTime)));
            }
        }
        //按时间降序排序
        Query query = Query.query(criteria).with(new Sort(new Sort.Order(Sort.Direction.DESC, "time")));
        Pagination<BaseBean> page = baseLogExportImpl.getPageLog(iDisplayStart / pageSize + 1, pageSize, tablename, query);
        printWriter.write(beanToJson(page));
        printWriter.flush();
        printWriter.close();
    }

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping(value = "log/index.do", method = RequestMethod.GET)
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping(value = "log/clientMonitor2.do")
    public ModelAndView clientMonitor() {
        List<List<String>> clientsStatus = new ArrayList<List<String>>();
        Map<String, String> tables = getClientTableMap();
        for (String client : tables.keySet()) {
            String tempClient = client.replace(Constants.BASE_LOG_ALL, "");
            List<String> temp = new ArrayList<String>();
            DBObject dbObject = new BasicDBObject();
            dbObject.put("clientId", tempClient);
            dbObject.put("sendDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            dbObject = mongoTemplate.getCollection(Constants.CLIENT_RUNTIME_INFO).findOne(dbObject);
            if (dbObject == null) continue;
            System.err.println(dbObject);
            temp.add(tempClient);
            temp.add(tables.get(client));
            temp.add(dbObject.get("clientStartTime").toString());
            temp.add(dbObject.get("clientLastTime").toString());
            temp.add(dbObject.get("serverLastTime").toString());
            temp.add(dbObject.get("exceptions").toString());
            clientsStatus.add(temp);
        }
        return new ModelAndView("clientMonitor").addObject("clients", tables).addObject("clientsStatus", clientsStatus);
    }

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping(value = "log/clientMonitor.do")
    public void clientMonitor2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        List clientsInfo = new ArrayList();
        PrintWriter printWriter = response.getWriter();
        //获取所有的client-id
        List<String> list = mongoTemplate.getCollection(Constants.CLIENT_RUNTIME_INFO).distinct("clientId");
        //遍历查询每个client的状态
        for (String client : list) {
            Criteria criteria = Criteria.where("clientId").is(client);
            Query query = Query.query(criteria).with(new Sort(new Sort.Order(Sort.Direction.DESC, "_id"))).limit(1);
            ClientInfoBean clientInfoBean = mongoTemplate.findOne(query, ClientInfoBean.class, Constants.CLIENT_RUNTIME_INFO);
            if (StringUtil.isBlank(clientInfoBean.getException())) {
                if (!StringUtil.isBlank(clientInfoBean.getReceiveDate())) {
                    int result = org.leadin.common.util.DateUtil.dateCompareDate(DateUtil.reverse2Date(clientInfoBean.getReceiveDate()), new Date());
                    //int result=DateUtil.reverse2Date(clientInfoBean.getReceive_date()).compareTo(new Date());
                    if (result == 0) {
                        clientInfoBean.setStatus("正常");
                    } else if (result == -1) {
                        clientInfoBean.setStatus("异常");
                        clientInfoBean.setMsg("上次心跳日期为：" + clientInfoBean.getReceiveTime() + ",请检查客户端是否断线!");
                    }
                } else {
                    clientInfoBean.setStatus("异常");
                    clientInfoBean.setMsg("上次心跳日期为空!");
                }
            } else {
                clientInfoBean.setStatus("异常");
                clientInfoBean.setMsg("事件异常");
            }
            clientsInfo.add(clientInfoBean);
        }
       /* List<DBObject> clientsStatus=new ArrayList<DBObject>();
        DBObject dbObject =new BasicDBObject();
        dbObject.put("send_date", DateUtil.toDate(new Date()));
        DBCursor cursor = mongoTemplate.getCollection(Constants.CLIENT_RUNTIME_INFO).find(dbObject);
        while (cursor.hasNext()){
            dbObject = cursor.next();
            System.err.println(dbObject);
            String client_last_time=dbObject.get("server_last_time").toString();

            if(StringUtil.isBlank(client_last_time)){
                dbObject.put("status","error");
                dbObject.put("msg","缺少字段【client_last_time】");
            }else{

            }
            clientsStatus.add(dbObject);
        }*/
        Map<String, List> aaData = new HashMap<String, List>();
        aaData.put("aaData", clientsInfo);
        String json = JSONObject.toJSONString(aaData);
        System.err.println(json);
        printWriter.write(json);
        printWriter.flush();
        printWriter.close();
    }

    private Map<String, String> getClientTableMap() {
        //获取db中所有存储原日志的表名
        Set<String> names = mongoTemplate.getCollectionNames();
        //获取配置文件中的服务
        ConfigUtil.load("tablenames.properties");
        Set<String> set = ConfigUtil.keySet();
        Map<String, String> tables = new HashMap<String, String>();
        for (String name : names) {
            //判断是否存储原始日志内容的表
            if (name.endsWith(Constants.BASE_LOG_ALL)) {
                String cName = ConfigUtil.getProperty(name.replace(Constants.BASE_LOG_ALL, ""));
                tables.put(name, (cName == null ? name.replace(Constants.BASE_LOG_ALL, "") : cName));
            } else if (name.endsWith(Constants.BASE_LOG_EXCEPTION)) {
                String cName = ConfigUtil.getProperty(name.replace(Constants.BASE_LOG_EXCEPTION, ""));
                tables.put(name, (cName == null ? name : cName + "异常库"));
            }
        }
        return tables;
    }

    /**
     * 组装datatables分页参数
     *
     * @param page
     * @return
     */
    private String beanToJson(Pagination<BaseBean> page) {
        Map map = new HashMap<>();
        map.put("iDisplayStart", page.getFirstResult());
        map.put("iTotalRecords", page.getTotalCount());
        map.put("iTotalDisplayRecords", page.getTotalCount());
        //List<List> data=new ArrayList<List>();
        for (BaseBean logBean : page.getDatas()) {
           /* List a=new ArrayList();
            a.add(logBean.getId());
            a.add(logBean.getTomcatName());
            a.add(logBean.getServerIp());
            //转义特殊字符
            a.add(HtmlUtils.htmlEscape(logBean.getContent()));
            data.add(a);*/
            logBean.setContent(HtmlUtils.htmlEscape(logBean.getContent()));
        }
        map.put("aaData", page.getDatas());
        String ss = JSONObject.toJSONStringWithDateFormat(map, "yyyy-MM-dd HH:mm:ss");
        LogUtil.debug("jsonStr={}", ss);
        return ss;
    }
}
