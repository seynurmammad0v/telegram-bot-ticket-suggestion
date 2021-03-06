package az.telegram.bot.exceptions;

public class IncorrectAnswerException extends Exception implements CustomException {

    String ru = "Пожалуйста, выберите или впишите правильный ответ!";
    String en = "Please select or enter the correct answer!";
    String az = "Zəhmət olmasa düzgün cavabı seçin və ya daxil edin!";

    @Override
    public String getAz() {
        return az;
    }

    @Override
    public String getRu() {
        return ru;
    }

    @Override
    public String getEn() {
        return en;
    }

}