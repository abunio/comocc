package com.occ.controller;

import com.occ.common.ExcelUtil;
import com.occ.common.POIUtil;
import com.occ.entity.User;
import com.occ.service.ExcelService;
import com.occ.service.UserService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.PageData;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private ExcelService excelService;


    /*
     * 功能描述:
     * 返回jsp视图展示
     * @Param: [user]
     * @Return: org.springframework.web.servlet.ModelAndView
     * @Author: abunio
     * @Date: 2019/5/14 16:18
     */
    @RequestMapping(value = "/getUserModel")
    public ModelAndView getUsers1(User user) {
        ModelAndView modelAndView = new ModelAndView();
        //调用service方法得到用户列表
        List<User> users = userService.getUsers(user);
        //将得到的用户列表内容添加到ModelAndView中
        modelAndView.addObject("users",users);
        //设置响应的jsp视图
        modelAndView.setViewName("UserList");
        return modelAndView;
    }

    /*
     * 功能描述:
     * 返回json格式数据
     * @Param: [user, request, response]
     * @Return: java.util.List
     * @Author: abunio
     * @Date: 2019/5/14 16:42
     */
    @RequestMapping(value = "/getUserJson",method = RequestMethod.GET)
    @ResponseBody
    public List getUsers2(User user, HttpServletRequest request, HttpServletResponse response) {
        //调用service方法得到用户列表
        List<User> users = userService.getUsers(user);
        return users;
    }


    /*
     * 功能描述:
     * 上传
     * @Param: [myFile]
     * @Return: java.lang.String
     * @Author: abunio
     * @Date: 2019/5/14 16:19
     */
    @RequestMapping(value="/upload",method = RequestMethod.POST )
    @ResponseBody
    public String uploadWesternMedicine(MultipartFile myFile) throws IOException {
        String flag ="0";
        try{
            List<String[]> list = POIUtil.readExcel(myFile); //这里得到的是一个集合，里面的每一个元素是String[]数组
            excelService.saveBath(list); //service实现方法
        } catch(Exception e){
            flag = "1";
        }
        return flag;
    }

    /*
    * 功能描述:
    * 导出
    * @Param: [user, request, response]
    * @Return: void
    * @Author: abunio
    * @Date: 2019/5/14 21:05
    */
    @RequestMapping(value = "/export")
    @ResponseBody
    public void export(User user,HttpServletRequest request,HttpServletResponse response) throws Exception {
        //获取数据
        List<User> list = userService.getUsers(user);

        //excel标题
        String[] title = {"姓名","生日","性别","地址"};

        //excel文件名
        String fileName = "个人信息表"+System.currentTimeMillis()+".xls";

        //sheet名
        String sheetName = "个人信息表";

        String [][] content = new String[list .size()][title.length];
        for (int i = 0; i < list .size(); i++) {
            content[i] = new String[title.length];
            User obj = list.get(i);
            content[i][0] = obj.getUsername().toString();
            content[i][1] = obj.getBirthday().toString();
            content[i][2] = obj.getSex().toString();
            content[i][3] = obj.getAddress().toString();

        }

        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}