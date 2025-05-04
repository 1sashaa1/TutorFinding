package com.jtspringproject.JtSpringProject.dto;
import java.util.List;

public class YouTubeResponse {
        private List<Item> items;

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }

        public static class Item {
            private Id id;
            private Snippet snippet;

            public Id getId() {
                return id;
            }

            public void setId(Id id) {
                this.id = id;
            }

            public Snippet getSnippet() {
                return snippet;
            }

            public void setSnippet(Snippet snippet) {
                this.snippet = snippet;
            }

            public static class Id {
                private String videoId;

                public String getVideoId() {
                    return videoId;
                }

                public void setVideoId(String videoId) {
                    this.videoId = videoId;
                }
            }

            public static class Snippet {
                private String title;
                private String description;

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }
            }
    }
}