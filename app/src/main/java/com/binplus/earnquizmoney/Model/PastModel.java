package com.binplus.earnquizmoney.Model;

import java.util.ArrayList;

public class PastModel {
    public PastContest past_contest;

    public PastContest getPast_contest() {
        return past_contest;
    }

    public void setPast_contest(PastContest past_contest) {
        this.past_contest = past_contest;
    }

    public class PastContest{
        public boolean error;
        public String message;
        public ArrayList<Datum> data;

        public boolean isError() {
            return error;
        }

        public void setError(boolean error) {
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
    }
    public class Datum{
        public String id;
        public String name;
        public String start_date;
        public String end_date;
        public String description;
        public String image;
        public String entry;
        public String max_entry;
        public String join_spot;
        public String available_spot;
        public String contest_status;
        public String prize_pool;
        public String date_created;
        public String top_users;
        public String participants;
        public int join_contest_status;
        public String complete_status;
        public ArrayList<Point> points;
        public ArrayList<CurrentFill> current_fill;

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

        public String getStart_date() {
            return start_date;
        }

        public void setStart_date(String start_date) {
            this.start_date = start_date;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getEntry() {
            return entry;
        }

        public void setEntry(String entry) {
            this.entry = entry;
        }

        public String getMax_entry() {
            return max_entry;
        }

        public void setMax_entry(String max_entry) {
            this.max_entry = max_entry;
        }

        public String getJoin_spot() {
            return join_spot;
        }

        public void setJoin_spot(String join_spot) {
            this.join_spot = join_spot;
        }

        public String getAvailable_spot() {
            return available_spot;
        }

        public void setAvailable_spot(String available_spot) {
            this.available_spot = available_spot;
        }

        public String getContest_status() {
            return contest_status;
        }

        public void setContest_status(String contest_status) {
            this.contest_status = contest_status;
        }

        public String getPrize_pool() {
            return prize_pool;
        }

        public void setPrize_pool(String prize_pool) {
            this.prize_pool = prize_pool;
        }

        public String getDate_created() {
            return date_created;
        }

        public void setDate_created(String date_created) {
            this.date_created = date_created;
        }

        public String getTop_users() {
            return top_users;
        }

        public void setTop_users(String top_users) {
            this.top_users = top_users;
        }

        public String getParticipants() {
            return participants;
        }

        public void setParticipants(String participants) {
            this.participants = participants;
        }

        public int getJoin_contest_status() {
            return join_contest_status;
        }

        public void setJoin_contest_status(int join_contest_status) {
            this.join_contest_status = join_contest_status;
        }

        public String getComplete_status() {
            return complete_status;
        }

        public void setComplete_status(String complete_status) {
            this.complete_status = complete_status;
        }

        public ArrayList<Point> getPoints() {
            return points;
        }

        public void setPoints(ArrayList<Point> points) {
            this.points = points;
        }

        public ArrayList<CurrentFill> getCurrent_fill() {
            return current_fill;
        }

        public void setCurrent_fill(ArrayList<CurrentFill> current_fill) {
            this.current_fill = current_fill;
        }
    }
    public class Point{
        public String top_winner;
        public String points;

        public String getTop_winner() {
            return top_winner;
        }

        public void setTop_winner(String top_winner) {
            this.top_winner = top_winner;
        }

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }
    }
    public class CurrentFill{
        public String top_winner;
        public String points;

        public String getTop_winner() {
            return top_winner;
        }

        public void setTop_winner(String top_winner) {
            this.top_winner = top_winner;
        }

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }
    }
}
