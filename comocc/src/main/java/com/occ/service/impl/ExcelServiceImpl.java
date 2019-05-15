package com.occ.service.impl;

import com.occ.dao.mapper.UserMapper;
import com.occ.entity.User;
import com.occ.service.ExcelService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {
    @Autowired
    private UserMapper userMapper;

    /*
     * 功能描述:
     * 此处亮点是日期转换
     * @Param: [list]
     * @Return: void
     * @Author: abunio
     * @Date: 2019/5/14 16:19
     */
    public void saveBath(List<String[]> list) throws ParseException {

        Calendar calendar = new GregorianCalendar(1900,0,-1);
        Date d = calendar.getTime();
        for (String[] strings : list) {
            User user = new User();
            user.setUsername(strings[0]);
            user.setBirthday(strings[1].equals("")?null:DateUtils.addDays(d,Integer.valueOf(strings[1])));
            if(strings[2].equals("男")){
                user.setSex("1");
            }else if (strings[2].equals("女")){
                user.setSex("2");
            }
            user.setAddress(strings[3]);
            userMapper.insertSelective(user);
        }
    }


}
