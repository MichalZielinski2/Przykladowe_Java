package sample;

import java.io.Serializable;

public class Score implements Serializable {
    private Integer score;
    private String person;

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public Integer getScore() {
        return score;
    }

    public String getPerson() {
        return person;
    }

    @Override
    public String toString() {
        return person+" : "+score;
    }

    public Score(int score, String person) {
        this.score = score;
        this.person = person;
    }
}
