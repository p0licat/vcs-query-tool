/*
 * Copyright 2016-2017 Testify Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ibm.gathererservicearchetype.try1.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.testifyproject.annotation.CollaboratorProvider;
import org.testifyproject.annotation.Sut;
import org.testifyproject.junit4.UnitTest;

/**
 * <p>
 * A unit test that demonstrates the ability to explictly specify the collaborators of the class
 * under test. This is sometimes necessary due collaborators being final classes (i.e. UUID)
 * that can not be mocked due to JVM restrictions.
 * </p>
 * <p>
 * Notice the {@link Collaborators} annotation on {@link #collaborators()} method which test
 * testify how to create an instance of the class under test with the given collaborators. That
 * the name of the method does not matter but it must not take parameters and must return an
 * array of objects.
 * </p>
 *
 * @author saden
 */
@RunWith(UnitTest.class)
public class GreetingModelTest {

    @Sut
    GreetingModel sut;

    @CollaboratorProvider
    Object[] collaborators() {
        return new Object[]{
            "hello"
        };
    }

    @Test
    public void givenPhraseNewGreetingModelShouldCreateInstance() {
        //Arrange
        String phrase = "hello";

        //Act
        GreetingModel model = new GreetingModel(phrase);

        //Assert
        assertThat(model.getPhrase()).isEqualTo(phrase);
    }

    @Test
    public void callToGetPhraseShouldReturnPhrase() {
        //Arrange
        String phrase = "hello";

        //Act
        String result = sut.getPhrase();

        //Assert
        assertThat(result).isEqualTo(phrase);
    }

    @Test
    public void callToSetPhraseShouldSetPhrase() {
        //Arrange
        String phrase = "hello";

        //Act
        sut.setPhrase(phrase);

        //Assert
        assertThat(sut.getPhrase()).isEqualTo(phrase);
    }

    @Test
    public void givenEqualGreetingsEqualsShouldReturnTrue() {
        //Arrange
        String phrase = "hello";
        GreetingModel model = new GreetingModel(phrase);
        //Act
        boolean result = sut.equals(model);

        //Assert
        assertThat(result).isTrue();
    }

    @Test
    public void givenUnequalGreetingsEqualsShouldReturnTrue() {
        //Arrange
        String phrase = "ciao";
        GreetingModel model = new GreetingModel(phrase);

        //Act
        boolean result = sut.equals(model);

        //Assert
        assertThat(result).isFalse();
    }

    @Test
    public void givenNullGreetingEqualsShouldReturnFalse() {
        //Arrange
        GreetingModel model = null;

        //Act
        boolean result = sut.equals(model);

        //Assert
        assertThat(result).isFalse();
    }

    @Test
    public void givenObjectEqualsShouldReturnFalse() {
        //Arrange
        Object object = new Object();

        //Act
        boolean result = sut.equals(object);

        //Assert
        assertThat(result).isFalse();
    }

    @Test
    public void givenSameObjectEqualsShouldReturnFalse() {
        //Act
        boolean result = sut.equals(sut);

        //Assert
        assertThat(result).isTrue();
    }

    @Test
    public void givenEqualGreetingsHashCodeShouldBeTheSame() {
        //Arrange
        String phrase = "hello";
        GreetingModel model = new GreetingModel(phrase);
        //Act
        int result = sut.hashCode();

        //Assert
        assertThat(result).isEqualTo(model.hashCode());
    }

    @Test
    public void callToStringShouldContaineridAndPhrase() {
        //Arrange
        String phrase = "hello";

        //Act
        String result = sut.toString();

        //Assert
        assertThat(result).contains(phrase);
    }
}
