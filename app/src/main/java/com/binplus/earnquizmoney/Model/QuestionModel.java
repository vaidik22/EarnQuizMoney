package com.binplus.earnquizmoney.Model;

import java.util.ArrayList;

public class QuestionModel {
    public String error;
    public String message;
    public ArrayList<Datum> data;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Datum> getData() {
        return data;
    }

    public void setData(ArrayList<Datum> data) {
        this.data = data;
    }

    public class Datum{
        public String id;
        public String contest_id;
        public String image;
        public String question;
        public String question_type;
        public String optiona;
        public String optionb;
        public String optionc;
        public String optiond;
        public String optione;
        public String answer;
        public String note;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContest_id() {
            return contest_id;
        }

        public void setContest_id(String contest_id) {
            this.contest_id = contest_id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getQuestion_type() {
            return question_type;
        }

        public void setQuestion_type(String question_type) {
            this.question_type = question_type;
        }

        public String getOptiona() {
            return optiona;
        }

        public void setOptiona(String optiona) {
            this.optiona = optiona;
        }

        public String getOptionb() {
            return optionb;
        }

        public void setOptionb(String optionb) {
            this.optionb = optionb;
        }

        public String getOptionc() {
            return optionc;
        }

        public void setOptionc(String optionc) {
            this.optionc = optionc;
        }

        public String getOptiond() {
            return optiond;
        }

        public void setOptiond(String optiond) {
            this.optiond = optiond;
        }

        public String getOptione() {
            return optione;
        }

        public void setOptione(String optione) {
            this.optione = optione;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }
    }
}
