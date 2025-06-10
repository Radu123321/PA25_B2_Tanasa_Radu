package org.example;

import java.lang.annotation.*;

/*
    Specifica faptul că adnotarea va fi disponibila la runtime (necesar pentru reflection).
     !!!!!Adnotarea se poate aplica doar pe metode.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
}
