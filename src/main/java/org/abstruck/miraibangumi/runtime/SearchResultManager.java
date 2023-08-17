package org.abstruck.miraibangumi.runtime;

import java.util.HashMap;
import java.util.Map;

import org.abstruck.miraibangumi.data.SearchData;

import net.mamoe.mirai.contact.Contact;

public class SearchResultManager {
    public static final SearchResultManager INSTANCE = new SearchResultManager();
    private Map<Contact,SearchData> lastSearch;
    private Map<Contact,Integer> page;

    public SearchResultManager(){
        this.lastSearch = new HashMap<>();
        this.page = new HashMap<>();
    }

    public void next(Contact contact,SearchData searchData){
        lastSearch.put(contact, searchData);
        page.put(contact, page.getOrDefault(contact, 1)+1);
    }
    public void searched(Contact c,SearchData searchData){
        lastSearch.put(c, searchData);
        page.put(c,2);
    }

    public SearchData getLastSearc(Contact c){
        return this.lastSearch.get(c);
    }

    public int getPage(Contact contact){
        return this.page.getOrDefault(contact, 1);
    }
}
