package zerobase.weather.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor //new Diary를 만들고 싶다.
@AllArgsConstructor //한번에 모든 컬럼을 넣는 경우
public class Diary {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String weather;
    private String icon;
    private String text;
    private LocalDate date;
    private double temperature;


    public void setDateWeather(DateWeather dateWeather){
        this.date = dateWeather.getDate();
        this.weather = dateWeather.getWeather();
        this.icon = dateWeather.getIcon();
        this.temperature = dateWeather.getTemperature();
    }
}
