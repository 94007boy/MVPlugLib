package com.djjie.mvpluglib.model;

import com.djjie.mvpluglib.MVPlug;
import com.djjie.mvpluglib.MVPlugConfig;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by xiaolv on 16/9/8.
 */
public class ResBodyGsonConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;
    private final MVPlugConfig mvPlugConfig;

    ResBodyGsonConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
        mvPlugConfig = MVPlug.getInstance().getConfiguration();
    }

    @Override
    public T convert(ResponseBody value) throws IOException{
        String originRes = value.string();
        MVPlugBaseResp resultResponse = gson.fromJson(originRes, MVPlugBaseResp.class);
        if (resultResponse.getCode() == mvPlugConfig.RES_SUCCESS_CODE()){
            //result==0表示成功返回，继续用本来的Model类解析
            if (mvPlugConfig.ismIsDebugMode()){
                Logger.d(originRes);
            }
            return gson.fromJson(originRes, type);
        } else {
            throw new MVPlugFailReason(resultResponse.getMsg(),Integer.valueOf(resultResponse.getCode()));
        }
    }
}