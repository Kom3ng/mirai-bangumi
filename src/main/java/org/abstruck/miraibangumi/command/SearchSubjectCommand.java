package org.abstruck.miraibangumi.command;

import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.OnlineMessageSource.Outgoing;

import org.abstruck.miraibangumi.MiraiBangumi;
import org.abstruck.miraibangumi.data.BangumiItem;
import org.abstruck.miraibangumi.data.SearchData;
import org.abstruck.miraibangumi.data.SearchResult;
import org.abstruck.miraibangumi.data.SubjectType;
import org.abstruck.miraibangumi.data.SubjectType.Type;
import org.abstruck.miraibangumi.exception.TypeIdException;
import org.abstruck.miraibangumi.runtime.SearchResultManager;
import org.abstruck.miraibangumi.util.SearchUtil;
import org.abstruck.miraibangumi.util.UrlBuilder;

/**
 * 烂代码不要动
 * 
 * @author Astrack
 * @date 2023/8/14
 */
public class SearchSubjectCommand extends JRawCommand {
    public SearchSubjectCommand() {
        super(MiraiBangumi.INSTANCE,"search");
        setDescription("搜索条目");
        setUsage("/search <keyword> (type)");
    }

    @Override
    public void onCommand(CommandContext commandContext,MessageChain args){
        CommandSender commandSender = commandContext.getSender();
        if(args.size() < 1){
            commandSender.getSubject().sendMessage(new PlainText("Usage: "+getUsage()));
            return;
        }
        String keyword = args.get(0).contentToString();
        String type = null;
        String maxResults = "10";
        if (args.size()>1){
            type = args.get(1).contentToString();
            try{
                int typeInt = Integer.parseInt(type);

                if (!(typeInt>0&&typeInt<7)){
                    throw new TypeIdException("类型参数必须在1-6之间");
                }
            } catch (NumberFormatException | TypeIdException e){
                commandSender.getSubject().sendMessage(new PlainText("错误:"+e.getMessage()));
                return;
            }
    
        }

        UrlBuilder urlBuilder = SearchUtil.INSTANCE.generateUrlBuilder(keyword, type, null, maxResults);

        SearchResult searchResult = SearchUtil.INSTANCE.search(urlBuilder);
    

        BangumiItem[] list = searchResult.list();
        StringBuilder sb = new StringBuilder("找到"+searchResult.results()+"个结果:\n\n");
        int index = 1;
        for (int i = 0; i < list.length; i++) {
            Type subjectType = SubjectType.byId(list[i].getType());
            sb.append(index).append('.').append(subjectType.name()).append(" ").append(list[i].getName()).append('\n');
            index++;
        }
        sb.append("\n第").append(1).append('页').append(" 共").append((int) Math.ceil(searchResult.results()/9)+1).append('页');
        PlainText resultMessage = new PlainText(sb.toString());
            
        Outgoing source = commandSender.getSubject().sendMessage(resultMessage).getSource();

        SearchResultManager.INSTANCE.searched(commandSender.getSubject(),new SearchData(urlBuilder, searchResult, source));
    }
}
