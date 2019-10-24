package com.example.libraryqa_android;

import java.util.List;
import android.graphics.Bitmap;

public class Info {
    String first;//知识库回答
    String second;//信息检索回答
    private List<SearchAsw> search_answer;//信息检索回答

    public class SearchAsw {
        String answer;
        String question;
        String score;

        public String getQuestion(){
            return question;
        }
        public String getAnswer(){
            return answer;
        }
        public String getScore(){
            return score;
        }

        public void setQuestion(String question) {
            this.question = question;
        }
        public void setAnswer(String answer){
            this.question = answer;
        }
        public void setScore(String score){
            this.score = score;
        }
    }

    public void setFirst(String first) {
        this.first = first;
    }
    public void setSecond(String second) {
        this.second = second;
    }
    public String getFirst(){
        return first;
    }
    public String getSecond() { return second;}
    public void setSearch_answer(List<SearchAsw> search_answer){
        this.search_answer = search_answer;
    }
    public List<SearchAsw> getSearch_answer(){
        return search_answer;
    }
    public void setfirstNull(){
        this.first = null;
    }
    public void setSecondNull(){
        this.second = null;
    }
}
