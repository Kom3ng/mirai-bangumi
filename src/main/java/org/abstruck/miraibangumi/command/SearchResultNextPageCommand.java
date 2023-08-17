package org.abstruck.miraibangumi.command;

import org.abstruck.miraibangumi.MiraiBangumi;
import org.abstruck.miraibangumi.data.BangumiItem;
import org.abstruck.miraibangumi.data.SearchData;
import org.abstruck.miraibangumi.data.SearchResult;
import org.abstruck.miraibangumi.data.SubjectType;
import org.abstruck.miraibangumi.data.SubjectType.Type;
import org.abstruck.miraibangumi.runtime.SearchResultManager;
import org.abstruck.miraibangumi.util.SearchUtil;
import org.abstruck.miraibangumi.util.UrlBuilder;

import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.OnlineMessageSource.Outgoing;
import net.mamoe.mirai.message.data.PlainText;

/**
 * 烂代码不要动
 */
public class SearchResultNextPageCommand extends JRawCommand {

    public SearchResultNextPageCommand() {
        super(MiraiBangumi.INSTANCE, "next","n");
        setUsage("/next");
        setDescription("搜索下一页");        
    }

    @Override
    public void onCommand(CommandContext commandContext,MessageChain args){
        SearchData searchData = SearchResultManager.INSTANCE.getLastSearc(commandContext.getSender().getSubject());                                            
        if(searchData==null){
            commandContext.getSender().getSubject().sendMessage(new PlainText("请先搜索"));
            return;
        }

        int start = Integer.parseInt(searchData.url().getQueryParamentOr("start", () -> "0"));
        int maxResults = Integer.parseInt(searchData.url().getQueryParamentOr("max_results", ()->"10"));

        start = start + maxResults;

        UrlBuilder urlBuilder = searchData.url();
        urlBuilder.setQueryParament("start", Integer.toString(start));
        urlBuilder.setQueryParament("max_results", Integer.toString(maxResults));

        SearchResult data = SearchUtil.INSTANCE.search(urlBuilder);

       BangumiItem[] list = data.list();
        StringBuilder sb = new StringBuilder("找到"+data.results()+"个结果:\n\n");
        int index = 1;
        for (int i = 0; i < list.length; i++) {
            Type subjectType = SubjectType.byId(list[i].getType());
            sb.append(index).append('.').append(subjectType.name()).append(" ").append(list[i].getName()).append('\n');
            index++;
        }
        sb.append("\n第").append(SearchResultManager.INSTANCE.getPage(commandContext.getSender().getSubject())).append('页').append(" 共").append((int)Math.ceil(data.results()/9)+1).append('页');
        PlainText resultMessage = new PlainText(sb.toString());

        Outgoing source = commandContext.getSender().getSubject().sendMessage(resultMessage).getSource();

        SearchResultManager.INSTANCE.next(commandContext.getSender().getSubject(),new SearchData(urlBuilder, data, source));
    }
    
}
