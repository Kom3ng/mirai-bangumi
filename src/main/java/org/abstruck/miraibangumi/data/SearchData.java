package org.abstruck.miraibangumi.data;

import org.abstruck.miraibangumi.util.UrlBuilder;

import net.mamoe.mirai.message.data.MessageSource;

public record SearchData(UrlBuilder url,SearchResult result,MessageSource message) {

}
