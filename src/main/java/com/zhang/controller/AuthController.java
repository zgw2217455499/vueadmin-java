package com.zhang.controller;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import com.zhang.common.lang.Const;
import com.zhang.common.lang.Result;
import com.zhang.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import sun.misc.BASE64Encoder;

/**
 * 验证码
 */
@RestController
public class AuthController {
    @Autowired
    private Producer producer;
    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/captcha")
    public Result captcha() throws IOException {
        //创建随机验证码
        String code = producer.createText();
        //创建图像
        BufferedImage image = producer.createImage(code);
        //方式一 创建输入流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        //方式二 使用File
        //ImageIO.write(image,"jpg",new FileImageOutputStream(new File()));
        //将流转换为base64
        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";
        String base64Img = str + encoder.encode(baos.toByteArray());
        String key = UUID.randomUUID().toString();
        redisUtil.hset(Const.CAPTCHA_KEY, key, base64Img, 120);
        return Result.succ(
                MapUtil.builder()
                        .put("token", key)
                        .put("captchaImg", base64Img)
                        .build());
    }
}
