package com.binplus.earnquizmoney.Model;

public class SignUpModel {

        public boolean response;
        public int otp;

        public boolean isResponse() {
            return response;
        }

        public void setResponse(boolean response) {
            this.response = response;
        }

        public int getOtp() {
            return otp;
        }

        public void setOtp(int otp) {
            this.otp = otp;
        }
}
