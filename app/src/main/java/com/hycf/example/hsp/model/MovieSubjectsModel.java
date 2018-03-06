package com.hycf.example.hsp.model;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Won on 2017/4/10.
 */

public class MovieSubjectsModel extends DataSupport implements Serializable {

    /**
     * rating : {"max":10,"average":7.8,"stars":"40","min":0}
     * genres : ["动画","短片","动作"]
     * title : 喜剧
     * casts : [{"alt":"https://movie.douban.com/celebrity/1048584/","avatars":{"small":"https://img3.doubanio.com/img/celebrity/small/27393.jpg","large":"https://img3.doubanio.com/img/celebrity/large/27393.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/27393.jpg"},"name":"绿川光","id":"1048584"},{"alt":"https://movie.douban.com/celebrity/1028508/","avatars":{"small":"https://img3.doubanio.com/img/celebrity/small/1361592268.1.jpg","large":"https://img3.doubanio.com/img/celebrity/large/1361592268.1.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/1361592268.1.jpg"},"name":"前田爱","id":"1028508"}]
     * collect_count : 3616
     * original_title : 喜劇
     * subtype : movie
     * directors : [{"alt":"https://movie.douban.com/celebrity/1321065/","avatars":{"small":"https://img3.doubanio.com/img/celebrity/small/52041.jpg","large":"https://img3.doubanio.com/img/celebrity/large/52041.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/52041.jpg"},"name":"中泽一登","id":"1321065"}]
     * year : 2002
     * images : {"small":"https://img5.doubanio.com/spic/s3823996.jpg","large":"https://img5.doubanio.com/lpic/s3823996.jpg","medium":"https://img5.doubanio.com/mpic/s3823996.jpg"}
     * alt : https://movie.douban.com/subject/3731342/
     * id : 3731342
     */

    private RatingBean rating;
    private String title;
    private int collect_count;
    private String original_title;
    private String subtype;
    private String year;
    private ImagesBean images;
    private String alt;
    @SerializedName("id")
    private String movie_id;
    private List<String> genres;
    private List<CastsBean> casts;
    private List<DirectorsBean> directors;

    public MovieDetailModel getMovieDetailModel() {
        return movieDetailModel;
    }

    public void setMovieDetailModel(MovieDetailModel movieDetailModel) {
        this.movieDetailModel = movieDetailModel;
    }

    private MovieDetailModel movieDetailModel;


    public RatingBean getRating() {
        return rating;
    }

    public void setRating(RatingBean rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCollect_count() {
        return collect_count;
    }

    public void setCollect_count(int collect_count) {
        this.collect_count = collect_count;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public ImagesBean getImages() {
        return images;
    }

    public void setImages(ImagesBean images) {
        this.images = images;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<CastsBean> getCasts() {
        return casts;
    }

    public void setCasts(List<CastsBean> casts) {
        this.casts = casts;
    }

    public List<DirectorsBean> getDirectors() {
        return directors;
    }

    public void setDirectors(List<DirectorsBean> directors) {
        this.directors = directors;
    }

    public static class RatingBean extends DataSupport implements Serializable{
        /**
         * max : 10
         * average : 7.8
         * stars : 40
         * min : 0
         */

        private int max;
        private double average;
        private String stars;
        private int min;

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public double getAverage() {
            return average;
        }

        public void setAverage(double average) {
            this.average = average;
        }

        public String getStars() {
            return stars;
        }

        public void setStars(String stars) {
            this.stars = stars;
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }
    }

    public static class ImagesBean extends DataSupport implements Serializable{
        /**
         * small : https://img5.doubanio.com/spic/s3823996.jpg
         * large : https://img5.doubanio.com/lpic/s3823996.jpg
         * medium : https://img5.doubanio.com/mpic/s3823996.jpg
         */

        private String small;
        private String large;
        private String medium;

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }
    }

    public static class CastsBean extends DataSupport implements Serializable{
        /**
         * alt : https://movie.douban.com/celebrity/1048584/
         * avatars : {"small":"https://img3.doubanio.com/img/celebrity/small/27393.jpg","large":"https://img3.doubanio.com/img/celebrity/large/27393.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/27393.jpg"}
         * name : 绿川光
         * id : 1048584
         */

        private String alt;
        private AvatarsBean avatars;
        private String name;
        @SerializedName("id")
        private String cast_id;
        private MovieSubjectsModel movieSubjectsModel;

        public MovieSubjectsModel getMovieSubjectsModel() {
            return movieSubjectsModel;
        }

        public void setMovieSubjectsModel(MovieSubjectsModel movieSubjectsModel) {
            this.movieSubjectsModel = movieSubjectsModel;
        }

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }

        public AvatarsBean getAvatars() {
            return avatars;
        }

        public void setAvatars(AvatarsBean avatars) {
            this.avatars = avatars;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return cast_id;
        }

        public void setId(String cast_id) {
            this.cast_id = cast_id;
        }

        public static class AvatarsBean extends DataSupport implements Serializable{
            /**
             * small : https://img3.doubanio.com/img/celebrity/small/27393.jpg
             * large : https://img3.doubanio.com/img/celebrity/large/27393.jpg
             * medium : https://img3.doubanio.com/img/celebrity/medium/27393.jpg
             */

            private String small;
            private String large;
            private String medium;

            public String getSmall() {
                return small;
            }

            public void setSmall(String small) {
                this.small = small;
            }

            public String getLarge() {
                return large;
            }

            public void setLarge(String large) {
                this.large = large;
            }

            public String getMedium() {
                return medium;
            }

            public void setMedium(String medium) {
                this.medium = medium;
            }
        }
    }

    public static class DirectorsBean extends DataSupport implements Serializable{
        /**
         * alt : https://movie.douban.com/celebrity/1321065/
         * avatars : {"small":"https://img3.doubanio.com/img/celebrity/small/52041.jpg","large":"https://img3.doubanio.com/img/celebrity/large/52041.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/52041.jpg"}
         * name : 中泽一登
         * id : 1321065
         */

        private String alt;
        private AvatarsBeanX avatars;
        private String name;
        @SerializedName("id")
        private String director_id;
        private MovieSubjectsModel movieSubjectsModel;

        public MovieSubjectsModel getMovieSubjectsModel() {
            return movieSubjectsModel;
        }

        public void setMovieSubjectsModel(MovieSubjectsModel movieSubjectsModel) {
            this.movieSubjectsModel = movieSubjectsModel;
        }

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }

        public AvatarsBeanX getAvatars() {
            return avatars;
        }

        public void setAvatars(AvatarsBeanX avatars) {
            this.avatars = avatars;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return director_id;
        }

        public void setId(String director_id) {
            this.director_id = director_id;
        }

        public static class AvatarsBeanX extends DataSupport implements Serializable{
            /**
             * small : https://img3.doubanio.com/img/celebrity/small/52041.jpg
             * large : https://img3.doubanio.com/img/celebrity/large/52041.jpg
             * medium : https://img3.doubanio.com/img/celebrity/medium/52041.jpg
             */

            private String small;
            private String large;
            private String medium;

            public String getSmall() {
                return small;
            }

            public void setSmall(String small) {
                this.small = small;
            }

            public String getLarge() {
                return large;
            }

            public void setLarge(String large) {
                this.large = large;
            }

            public String getMedium() {
                return medium;
            }

            public void setMedium(String medium) {
                this.medium = medium;
            }
        }
    }
}
