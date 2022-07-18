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
package org.ibm.gathererservicearchetype.try1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.testifyproject.annotation.Fake;
import org.testifyproject.annotation.Sut;
import org.testifyproject.junit4.UnitTest;

import org.ibm.gathererservicearchetype.try1.model.GreetingModel;

/**
 * A unit test that demonstrates the ability to verify class under test method calls by setting
 * {@link Sut#value()} to true.
 *
 * @author saden
 */
@RunWith(UnitTest.class)
public class ListGreetingsTest {

    @Sut(true)
    ListGreetings sut;

    @Fake
    Map<UUID, GreetingModel> store;

    @Test
    public void givenMapStoreNewListGreetingShouldNotDoWorkInConstructor() {
        //Act
        ListGreetings result = new ListGreetings(store);

        //Assert
        assertThat(result).isNotNull();
        verifyZeroInteractions(store);
    }

    @Test
    public void givenEmptyStoreListGreetingShouldReturnAnEmptyCollection() {
        //Act
        Collection<GreetingModel> result = sut.listGreetings();

        //Assert
        assertThat(result).isEmpty();
        verify(store).values();

        //We can verify calls to the class under test
        verify(sut).listGreetings();
    }

    @Test
    public void givenStoreGreetingsListGreetingShouldReturnCollectionOfGreetings() {
        //Arrange
        @SuppressWarnings("unchecked")
        Collection<GreetingModel> greetings = mock(Collection.class);
        given(store.values()).willReturn(greetings);

        //Act
        Collection<GreetingModel> result = sut.listGreetings();

        //Assert
        assertThat(result).isSameAs(greetings);
        verify(store).values();

        //We can verify calls to the class under test
        verify(sut).listGreetings();
    }

}
