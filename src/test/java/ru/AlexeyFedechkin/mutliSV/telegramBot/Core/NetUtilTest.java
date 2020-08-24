package ru.AlexeyFedechkin.mutliSV.telegramBot.Core;

import org.junit.Assert;
import org.junit.Test;

public class NetUtilTest {

    @Test
    public void ping() {
        Assert.assertTrue(NetUtil.ping("https://google.com"));
    }
}