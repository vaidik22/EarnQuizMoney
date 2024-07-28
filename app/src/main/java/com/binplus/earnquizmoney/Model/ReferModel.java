package com.binplus.earnquizmoney.Model;

import java.util.ArrayList;

public class ReferModel {
    public String error;
    public String message;
    public Data data;
    public ArrayList<Data> refer_history;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public ArrayList<Data> getRefer_history() {
        return refer_history;
    }

    public void setRefer_history(ArrayList<Data> refer_history) {
        this.refer_history = refer_history;
    }

    public class Data{
        public String id;
        public String name;
        public String wallet;
        public String own_code;
        public String total_refer_count;
        public String total_played_game;
        public String total_amount_spend;
        public String total_amount_earned;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getWallet() {
            return wallet;
        }

        public void setWallet(String wallet) {
            this.wallet = wallet;
        }

        public String getOwn_code() {
            return own_code;
        }

        public void setOwn_code(String own_code) {
            this.own_code = own_code;
        }

        public String getTotal_refer_count() {
            return total_refer_count;
        }

        public void setTotal_refer_count(String total_refer_count) {
            this.total_refer_count = total_refer_count;
        }

        public String getTotal_played_game() {
            return total_played_game;
        }

        public void setTotal_played_game(String total_played_game) {
            this.total_played_game = total_played_game;
        }

        public String getTotal_amount_spend() {
            return total_amount_spend;
        }

        public void setTotal_amount_spend(String total_amount_spend) {
            this.total_amount_spend = total_amount_spend;
        }

        public String getTotal_amount_earned() {
            return total_amount_earned;
        }

        public void setTotal_amount_earned(String total_amount_earned) {
            this.total_amount_earned = total_amount_earned;
        }
    }
}
