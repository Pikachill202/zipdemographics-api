// Converter.java

// To use this code, add the following Maven dependency to your project:
//
//
//     com.fasterxml.jackson.core     : jackson-databind          : 2.9.0
//     com.fasterxml.jackson.datatype : jackson-datatype-jsr310   : 2.9.0
//
// Import this package:
//
//     import com.apiverve.data.Converter;
//
// Then you can deserialize a JSON string with
//
//     ZIPDemographicsData data = Converter.fromJsonString(jsonString);

package com.apiverve.zipdemographics.data;

import java.io.IOException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class Converter {
    // Date-time helpers

    private static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendOptional(DateTimeFormatter.ISO_DATE_TIME)
            .appendOptional(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            .appendOptional(DateTimeFormatter.ISO_INSTANT)
            .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SX"))
            .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX"))
            .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            .toFormatter()
            .withZone(ZoneOffset.UTC);

    public static OffsetDateTime parseDateTimeString(String str) {
        return ZonedDateTime.from(Converter.DATE_TIME_FORMATTER.parse(str)).toOffsetDateTime();
    }

    private static final DateTimeFormatter TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendOptional(DateTimeFormatter.ISO_TIME)
            .appendOptional(DateTimeFormatter.ISO_OFFSET_TIME)
            .parseDefaulting(ChronoField.YEAR, 2020)
            .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .toFormatter()
            .withZone(ZoneOffset.UTC);

    public static OffsetTime parseTimeString(String str) {
        return ZonedDateTime.from(Converter.TIME_FORMATTER.parse(str)).toOffsetDateTime().toOffsetTime();
    }
    // Serialize/deserialize helpers

    public static ZIPDemographicsData fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    public static String toJsonString(ZIPDemographicsData obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(OffsetDateTime.class, new JsonDeserializer<OffsetDateTime>() {
            @Override
            public OffsetDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
                String value = jsonParser.getText();
                return Converter.parseDateTimeString(value);
            }
        });
        mapper.registerModule(module);
        reader = mapper.readerFor(ZIPDemographicsData.class);
        writer = mapper.writerFor(ZIPDemographicsData.class);
    }

    private static ObjectReader getObjectReader() {
        if (reader == null) instantiateMapper();
        return reader;
    }

    private static ObjectWriter getObjectWriter() {
        if (writer == null) instantiateMapper();
        return writer;
    }
}

// ZIPDemographicsData.java

package com.apiverve.zipdemographics.data;

import com.fasterxml.jackson.annotation.*;

public class ZIPDemographicsData {
    private String zip;
    private String name;
    private long acsYear;
    private Population population;
    private Income income;
    private Housing housing;
    private Education education;
    private Employment employment;
    private Race race;

    @JsonProperty("zip")
    public String getZip() { return zip; }
    @JsonProperty("zip")
    public void setZip(String value) { this.zip = value; }

    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("name")
    public void setName(String value) { this.name = value; }

    @JsonProperty("acsYear")
    public long getAcsYear() { return acsYear; }
    @JsonProperty("acsYear")
    public void setAcsYear(long value) { this.acsYear = value; }

    @JsonProperty("population")
    public Population getPopulation() { return population; }
    @JsonProperty("population")
    public void setPopulation(Population value) { this.population = value; }

    @JsonProperty("income")
    public Income getIncome() { return income; }
    @JsonProperty("income")
    public void setIncome(Income value) { this.income = value; }

    @JsonProperty("housing")
    public Housing getHousing() { return housing; }
    @JsonProperty("housing")
    public void setHousing(Housing value) { this.housing = value; }

    @JsonProperty("education")
    public Education getEducation() { return education; }
    @JsonProperty("education")
    public void setEducation(Education value) { this.education = value; }

    @JsonProperty("employment")
    public Employment getEmployment() { return employment; }
    @JsonProperty("employment")
    public void setEmployment(Employment value) { this.employment = value; }

    @JsonProperty("race")
    public Race getRace() { return race; }
    @JsonProperty("race")
    public void setRace(Race value) { this.race = value; }
}

// Education.java

package com.apiverve.zipdemographics.data;

import com.fasterxml.jackson.annotation.*;

public class Education {
    private double collegeEducatedPct;
    private long bachelors;
    private long masters;
    private long professional;
    private long doctorate;

    @JsonProperty("collegeEducatedPct")
    public double getCollegeEducatedPct() { return collegeEducatedPct; }
    @JsonProperty("collegeEducatedPct")
    public void setCollegeEducatedPct(double value) { this.collegeEducatedPct = value; }

    @JsonProperty("bachelors")
    public long getBachelors() { return bachelors; }
    @JsonProperty("bachelors")
    public void setBachelors(long value) { this.bachelors = value; }

    @JsonProperty("masters")
    public long getMasters() { return masters; }
    @JsonProperty("masters")
    public void setMasters(long value) { this.masters = value; }

    @JsonProperty("professional")
    public long getProfessional() { return professional; }
    @JsonProperty("professional")
    public void setProfessional(long value) { this.professional = value; }

    @JsonProperty("doctorate")
    public long getDoctorate() { return doctorate; }
    @JsonProperty("doctorate")
    public void setDoctorate(long value) { this.doctorate = value; }
}

// Employment.java

package com.apiverve.zipdemographics.data;

import com.fasterxml.jackson.annotation.*;

public class Employment {
    private long laborForce;
    private long unemployed;
    private double unemploymentRate;

    @JsonProperty("laborForce")
    public long getLaborForce() { return laborForce; }
    @JsonProperty("laborForce")
    public void setLaborForce(long value) { this.laborForce = value; }

    @JsonProperty("unemployed")
    public long getUnemployed() { return unemployed; }
    @JsonProperty("unemployed")
    public void setUnemployed(long value) { this.unemployed = value; }

    @JsonProperty("unemploymentRate")
    public double getUnemploymentRate() { return unemploymentRate; }
    @JsonProperty("unemploymentRate")
    public void setUnemploymentRate(double value) { this.unemploymentRate = value; }
}

// Housing.java

package com.apiverve.zipdemographics.data;

import com.fasterxml.jackson.annotation.*;

public class Housing {
    private long medianHomeValue;
    private long medianRent;
    private long totalUnits;
    private long occupiedUnits;
    private long vacantUnits;
    private long ownerOccupied;
    private long renterOccupied;
    private double homeOwnershipRate;

    @JsonProperty("medianHomeValue")
    public long getMedianHomeValue() { return medianHomeValue; }
    @JsonProperty("medianHomeValue")
    public void setMedianHomeValue(long value) { this.medianHomeValue = value; }

    @JsonProperty("medianRent")
    public long getMedianRent() { return medianRent; }
    @JsonProperty("medianRent")
    public void setMedianRent(long value) { this.medianRent = value; }

    @JsonProperty("totalUnits")
    public long getTotalUnits() { return totalUnits; }
    @JsonProperty("totalUnits")
    public void setTotalUnits(long value) { this.totalUnits = value; }

    @JsonProperty("occupiedUnits")
    public long getOccupiedUnits() { return occupiedUnits; }
    @JsonProperty("occupiedUnits")
    public void setOccupiedUnits(long value) { this.occupiedUnits = value; }

    @JsonProperty("vacantUnits")
    public long getVacantUnits() { return vacantUnits; }
    @JsonProperty("vacantUnits")
    public void setVacantUnits(long value) { this.vacantUnits = value; }

    @JsonProperty("ownerOccupied")
    public long getOwnerOccupied() { return ownerOccupied; }
    @JsonProperty("ownerOccupied")
    public void setOwnerOccupied(long value) { this.ownerOccupied = value; }

    @JsonProperty("renterOccupied")
    public long getRenterOccupied() { return renterOccupied; }
    @JsonProperty("renterOccupied")
    public void setRenterOccupied(long value) { this.renterOccupied = value; }

    @JsonProperty("homeOwnershipRate")
    public double getHomeOwnershipRate() { return homeOwnershipRate; }
    @JsonProperty("homeOwnershipRate")
    public void setHomeOwnershipRate(double value) { this.homeOwnershipRate = value; }
}

// Income.java

package com.apiverve.zipdemographics.data;

import com.fasterxml.jackson.annotation.*;

public class Income {
    private long medianHousehold;
    private long perCapita;

    @JsonProperty("medianHousehold")
    public long getMedianHousehold() { return medianHousehold; }
    @JsonProperty("medianHousehold")
    public void setMedianHousehold(long value) { this.medianHousehold = value; }

    @JsonProperty("perCapita")
    public long getPerCapita() { return perCapita; }
    @JsonProperty("perCapita")
    public void setPerCapita(long value) { this.perCapita = value; }
}

// Population.java

package com.apiverve.zipdemographics.data;

import com.fasterxml.jackson.annotation.*;

public class Population {
    private long total;
    private long male;
    private long female;
    private double medianAge;

    @JsonProperty("total")
    public long getTotal() { return total; }
    @JsonProperty("total")
    public void setTotal(long value) { this.total = value; }

    @JsonProperty("male")
    public long getMale() { return male; }
    @JsonProperty("male")
    public void setMale(long value) { this.male = value; }

    @JsonProperty("female")
    public long getFemale() { return female; }
    @JsonProperty("female")
    public void setFemale(long value) { this.female = value; }

    @JsonProperty("medianAge")
    public double getMedianAge() { return medianAge; }
    @JsonProperty("medianAge")
    public void setMedianAge(double value) { this.medianAge = value; }
}

// Race.java

package com.apiverve.zipdemographics.data;

import com.fasterxml.jackson.annotation.*;

public class Race {
    private Asian white;
    private Asian black;
    private Asian asian;
    private Asian hispanic;

    @JsonProperty("white")
    public Asian getWhite() { return white; }
    @JsonProperty("white")
    public void setWhite(Asian value) { this.white = value; }

    @JsonProperty("black")
    public Asian getBlack() { return black; }
    @JsonProperty("black")
    public void setBlack(Asian value) { this.black = value; }

    @JsonProperty("asian")
    public Asian getAsian() { return asian; }
    @JsonProperty("asian")
    public void setAsian(Asian value) { this.asian = value; }

    @JsonProperty("hispanic")
    public Asian getHispanic() { return hispanic; }
    @JsonProperty("hispanic")
    public void setHispanic(Asian value) { this.hispanic = value; }
}

// Asian.java

package com.apiverve.zipdemographics.data;

import com.fasterxml.jackson.annotation.*;

public class Asian {
    private long count;
    private double percent;

    @JsonProperty("count")
    public long getCount() { return count; }
    @JsonProperty("count")
    public void setCount(long value) { this.count = value; }

    @JsonProperty("percent")
    public double getPercent() { return percent; }
    @JsonProperty("percent")
    public void setPercent(double value) { this.percent = value; }
}