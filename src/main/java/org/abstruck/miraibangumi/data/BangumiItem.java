package org.abstruck.miraibangumi.data;

import org.abstruck.miraibangumi.util.SortMode;
import org.abstruck.miraibangumi.util.Sortable;

import com.google.gson.annotations.SerializedName;

/**
 * @author Astrack
 * @date 2023/8/13
 */
public class BangumiItem implements Sortable{
    private Integer id;
    private String url;
    private Integer type;
    private String name;
    private String name_cn;
    private String summary;
    private String air_date;
    private Integer air_weekday;
    private Images images;
    private Integer eps;
    private Integer eps_count;
    private Rating rating;
    private Integer rank;
    private Collection collection;

    @Override
    public int sortValue(SortMode mode){
        switch(mode){
            case COLLECT: return getCollection()==null?0:getCollection().collect;
            default: return 0;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_cn() {
        return name_cn;
    }

    public void setName_cn(String name_cn) {
        this.name_cn = name_cn;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAir_date() {
        return air_date;
    }

    public void setAir_date(String air_date) {
        this.air_date = air_date;
    }

    public Integer getAir_weekday() {
        return air_weekday;
    }

    public void setAir_weekday(Integer air_weekday) {
        this.air_weekday = air_weekday;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public Integer getEps() {
        return eps;
    }

    public void setEps(Integer eps) {
        this.eps = eps;
    }

    public Integer getEps_count() {
        return eps_count;
    }

    public void setEps_count(Integer eps_count) {
        this.eps_count = eps_count;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Collection getCollection(){
        return this.collection;
    }
    public void setCollection(Collection collection){
        this.collection = collection;
    }

    public static class Rating {
        private Integer total;
        private Count count;
        private Float score;

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public Count getCount() {
            return count;
        }

        public void setCount(Count count) {
            this.count = count;
        }

        public Float getScore() {
            return score;
        }

        public void setScore(Float score) {
            this.score = score;
        }


        public static class Count {
            @SerializedName("1")
            private
            Integer one;
            @SerializedName("2")
            private
            Integer two;
            @SerializedName("3")
            private
            Integer three;
            @SerializedName("4")
            private
            Integer four;
            @SerializedName("5")
            private
            Integer five;
            @SerializedName("6")
            private
            Integer six;
            @SerializedName("7")
            private
            Integer seven;
            @SerializedName("8")
            private
            Integer eight;
            @SerializedName("9")
            private
            Integer nine;
            @SerializedName("10")
            private
            Integer ten;


            public Integer getOne() {
                return one;
            }

            public void setOne(Integer one) {
                this.one = one;
            }

            public Integer getTwo() {
                return two;
            }

            public void setTwo(Integer two) {
                this.two = two;
            }

            public Integer getThree() {
                return three;
            }

            public void setThree(Integer three) {
                this.three = three;
            }

            public Integer getFour() {
                return four;
            }

            public void setFour(Integer four) {
                this.four = four;
            }

            public Integer getFive() {
                return five;
            }

            public void setFive(Integer five) {
                this.five = five;
            }

            public Integer getSix() {
                return six;
            }

            public void setSix(Integer six) {
                this.six = six;
            }

            public Integer getSeven() {
                return seven;
            }

            public void setSeven(Integer seven) {
                this.seven = seven;
            }

            public Integer getEight() {
                return eight;
            }

            public void setEight(Integer eight) {
                this.eight = eight;
            }

            public Integer getNine() {
                return nine;
            }

            public void setNine(Integer nine) {
                this.nine = nine;
            }

            public Integer getTen() {
                return ten;
            }

            public void setTen(Integer ten) {
                this.ten = ten;
            }
        }

    }

    public static class Collection {
        private Integer wish;
        private Integer collect;
        private Integer doing;
        private Integer on_hold;
        private Integer dropped;

        public Integer getWish() {
            return wish;
        }

        public void setWish(Integer wish) {
            this.wish = wish;
        }

        public Integer getCollect() {
            return collect;
        }

        public void setCollect(Integer collect) {
            this.collect = collect;
        }

        public Integer getDoing() {
            return doing;
        }

        public void setDoing(Integer doing) {
            this.doing = doing;
        }

        public Integer getOn_hold() {
            return on_hold;
        }

        public void setOn_hold(Integer on_hold) {
            this.on_hold = on_hold;
        }

        public Integer getDropped() {
            return dropped;
        }

        public void setDropped(Integer dropped) {
            this.dropped = dropped;
        }
    }
}
