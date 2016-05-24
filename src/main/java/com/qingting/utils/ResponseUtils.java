package com.qingting.utils;

import com.alibaba.fastjson.JSON;
import com.qingting.model.AjaxResponse;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 2016/5/24.
 */
public class ResponseUtils {

    private static Logger LOG= Logger.getLogger(ResponseUtils.class);

    public static void write(HttpServletResponse response,AjaxResponse ajaxResponse)throws IOException{
        try {
            response.getWriter().write(JSON.toJSONString(ajaxResponse));
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
           throw e;
        }
    }
}
