package alexa.skill.model.es;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import io.searchbox.core.Search;
import lombok.Getter;
import lombok.Setter;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by ranjiti on 11/12/16.
 */
public class SearchResult implements Comparable {
    @Getter @Setter
    private String _id;

    @Getter @Setter
    private double _score;

    @Getter @Setter
    private Book _source;

    @Override
    public int compareTo(Object that) {
        if (that instanceof SearchResult) {
            if (this._score > ((SearchResult) that)._score)
                return 1;
            else if (this._score < ((SearchResult) that)._score)
                return -1;
        }
        return 0;
    }

    public class Book {
        @Getter @Setter
        private String description;
        @Getter @Setter
        private String language;
        @Getter @Setter
        private String title;
        @Getter @Setter
        private double score;
        @Getter @Setter
        private String totalTime;

        @Getter @Setter
        private Sections sections;
        public class Sections {
            @Getter @Setter
            private List<Section> sectionUrls;
        }

        @Getter @Setter
        private int numSections;
        @Getter @Setter
        private Genres genres;
        class Genres {
            @Getter @Setter
            List<Genre> listOfGenres;
        }

        @Getter @Setter
        private Authors authors;
        class Authors {
            @Getter @Setter
            List<Author> listOfAuthors;
        }

        // leaf
        class Author {
            @Getter @Setter
            private String firstName;
            @Getter @Setter
            private String lastName;
            @Getter @Setter
            private String dob;
            @Getter @Setter
            private String dod;
            @Getter @Setter
            private int id;
        }

        // leaf
        public class Section implements Comparable{
            @Getter @Setter
            private int number;
            @Getter @Setter
            private String name;
            @Getter @Setter
            private String url;

            @Override
            public int compareTo(Object that) {
                if (that instanceof Section) {
                    if (this.number  > ((Section) that).number)
                        return 1;
                    else if (this.number < ((Section) that).number)
                        return -1;
                }
                return 0;
            }
        }

        // leaf
        class Genre {
            @Getter @Setter
            private String name;
        }
    }


    public static void main(String[] args) {


        String resultJson  = "{\n" +
                "            \"_index\": \"audiobooks\",\n" +
                "            \"_type\": \"book\",\n" +
                "            \"_id\": \"AVg9VG0wSHkeuxhZ9Kag\",\n" +
                "            \"_score\": 7.483617,\n" +
                "            \"_source\": {\n" +
                "               \"@class\": \"Book\",\n" +
                "               \"url_iarchive\": \"http://archive.org/details/dark_ls_1510_librivox\",\n" +
                "               \"zipFileUrl\": \"http://www.archive.org/download//dark_ls_1510_librivox/dark_ls_1510_librivox_64kb_mp3.zip\",\n" +
                "               \"@type\": \"d\",\n" +
                "               \"totalTime\": \"01:56:51\",\n" +
                "               \"numSections\": 5,\n" +
                "               \"description\": \"<i>The Dark</i> is a novella about a desperate young man, a “terrorist and nihilist”, trying to avoid arrest by taking refuge in a brothel. The story focuses on his unfolding relationship with a prostitute in the brothel and the internal conflict which torments him. The author, Leonid Andreyev, an acclaimed Russian playwright and writer of short fiction, was noted for the darkness in his work. This book was published by Leonard and Virginia Woolf.\\n( Lee Smalley)</p>\",\n" +
                "               \"language\": \"English\",\n" +
                "               \"title\": \"Dark\",\n" +
                "               \"sections\": {\n" +
                "                  \"listOfSections\": [\n" +
                "                     {\n" +
                "                        \"fileName\": \"dark_1_andreyev_128kb.mp3\",\n" +
                "                        \"listenUrl\": \"https://librivox.org/uploads/icequeen/dark_1_andreyev_128kb.mp3\",\n" +
                "                        \"sectionNumber\": 1,\n" +
                "                        \"playTime\": 1439,\n" +
                "                        \"id\": 349174,\n" +
                "                        \"title\": \"Section 1\",\n" +
                "                        \"lang\": \"English\"\n" +
                "                     },\n" +
                "                     {\n" +
                "                        \"fileName\": \"dark_2_andreyev_128kb.mp3\",\n" +
                "                        \"listenUrl\": \"https://librivox.org/uploads/icequeen/dark_2_andreyev_128kb.mp3\",\n" +
                "                        \"sectionNumber\": 2,\n" +
                "                        \"playTime\": 1456,\n" +
                "                        \"id\": 349175,\n" +
                "                        \"title\": \"Section 2\",\n" +
                "                        \"lang\": \"English\"\n" +
                "                     },\n" +
                "                     {\n" +
                "                        \"fileName\": \"dark_3_andreyev_128kb.mp3\",\n" +
                "                        \"listenUrl\": \"https://librivox.org/uploads/icequeen/dark_3_andreyev_128kb.mp3\",\n" +
                "                        \"sectionNumber\": 3,\n" +
                "                        \"playTime\": 1402,\n" +
                "                        \"id\": 349176,\n" +
                "                        \"title\": \"Section 3\",\n" +
                "                        \"lang\": \"English\"\n" +
                "                     },\n" +
                "                     {\n" +
                "                        \"fileName\": \"dark_4_andreyev_128kb.mp3\",\n" +
                "                        \"listenUrl\": \"https://librivox.org/uploads/icequeen/dark_4_andreyev_128kb.mp3\",\n" +
                "                        \"sectionNumber\": 4,\n" +
                "                        \"playTime\": 1424,\n" +
                "                        \"id\": 349177,\n" +
                "                        \"title\": \"Section 4\",\n" +
                "                        \"lang\": \"English\"\n" +
                "                     },\n" +
                "                     {\n" +
                "                        \"fileName\": \"dark_5_andreyev_128kb.mp3\",\n" +
                "                        \"listenUrl\": \"https://librivox.org/uploads/icequeen/dark_5_andreyev_128kb.mp3\",\n" +
                "                        \"sectionNumber\": 5,\n" +
                "                        \"playTime\": 1290,\n" +
                "                        \"id\": 349178,\n" +
                "                        \"title\": \"Section 5\",\n" +
                "                        \"lang\": \"English\"\n" +
                "                     }\n" +
                "                  ],\n" +
                "                  \"sectionUrls\": [\n" +
                "                     {\n" +
                "                        \"name\": \"01 - Section 1\",\n" +
                "                        \"url\": \"http://ia801407.us.archive.org/28/items/dark_ls_1510_librivox/dark_1_andreyev_64kb.mp3\"\n" +
                "                     },\n" +
                "                     {\n" +
                "                        \"name\": \"03 - Section 3\",\n" +
                "                        \"url\": \"http://ia801407.us.archive.org/28/items/dark_ls_1510_librivox/dark_3_andreyev_64kb.mp3\"\n" +
                "                     },\n" +
                "                     {\n" +
                "                        \"name\": \"04 - Section 4\",\n" +
                "                        \"url\": \"http://ia801407.us.archive.org/28/items/dark_ls_1510_librivox/dark_4_andreyev_64kb.mp3\"\n" +
                "                     },\n" +
                "                     {\n" +
                "                        \"name\": \"05 - Section 5\",\n" +
                "                        \"url\": \"http://ia801407.us.archive.org/28/items/dark_ls_1510_librivox/dark_5_andreyev_64kb.mp3\"\n" +
                "                     },\n" +
                "                     {\n" +
                "                        \"name\": \"02 - Section 2\",\n" +
                "                        \"url\": \"http://ia801407.us.archive.org/28/items/dark_ls_1510_librivox/dark_2_andreyev_64kb.mp3\"\n" +
                "                     }\n" +
                "                  ]\n" +
                "               },\n" +
                "               \"@rid\": \"#9:2761\",\n" +
                "               \"copyrightYear\": \"1922\",\n" +
                "               \"genres\": {\n" +
                "                  \"listOfGenres\": [\n" +
                "                     {\n" +
                "                        \"name\": \"Action & Adventure Fiction\",\n" +
                "                        \"id\": \"3\"\n" +
                "                     }\n" +
                "                  ]\n" +
                "               },\n" +
                "               \"@version\": 1,\n" +
                "               \"id\": \"10200\",\n" +
                "               \"librivoxUrl\": \"http://librivox.org/the-dark-by-leonid-nikolayevich-andreyev/\",\n" +
                "               \"authors\": {\n" +
                "                  \"listOfAuthors\": [\n" +
                "                     {\n" +
                "                        \"firstName\": \"Leonid Nikolayevich\",\n" +
                "                        \"lastName\": \"Andreyev\",\n" +
                "                        \"dob\": \"1871\",\n" +
                "                        \"dod\": \"1919\",\n" +
                "                        \"id\": 231\n" +
                "                     }\n" +
                "                  ]\n" +
                "               }\n" +
                "            }\n" +
                "         }";

        GsonBuilder builder = new GsonBuilder();
//        builder.registerTypeAdapter(new TypeToken<List<Book.Section>>(){}.getType(), new SectionsDeserializer());

        Gson gson = builder.create();

        JsonReader reader = new JsonReader(new StringReader(resultJson));

        SearchResult results = gson.fromJson(reader, SearchResult.class);
        System.out.println("Done");



    }

}
