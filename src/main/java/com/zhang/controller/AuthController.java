package com.zhang.controller;

import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import com.zhang.common.lang.Const;
import com.zhang.common.lang.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import sun.misc.BASE64Encoder;

/**
 * 验证码
 */
@RestController
public class AuthController extends BaseController {
    @Autowired
    Producer producer;

    @GetMapping("/captcha")
    public Result captcha() throws IOException {
        String key = UUID.randomUUID().toString();
        //创建随机验证码
        String code = producer.createText();
        key="aaaaa";
        code="aaaaa";
        //创建图像
        BufferedImage image = producer.createImage(code);
        //方式一 创建输入流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        //将流转换为base64
        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";
        String base64Img = str + encoder.encode(baos.toByteArray());



        redisUtil.hset(Const.CAPTCHA_KEY, key, code, 120);
        return Result.succ(
                MapUtil.builder()
                        .put("token", key)
                        .put("captchaImg", base64Img)
                        .build());
    }
}
