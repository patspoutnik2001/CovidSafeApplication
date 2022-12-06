package com.example.covidsafeapplication;

import static com.itextpdf.text.factories.GreekAlphabetFactory.getString;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LocalActivityTest {

    @Test
    public void onCreate() {
    }

    @Test
    public void displayMesures(String _type) {
        if (!_type.isEmpty() || _type !=null){
            assertEquals(_type,"Co2");
            assertEquals(_type,"Temperature");
            //il manque le humidity, on peut pas car le string provient du fichier string

        }else{
            assertNotNull(_type);
        }

    }
}