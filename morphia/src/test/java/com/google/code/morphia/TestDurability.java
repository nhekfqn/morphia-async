/**
 * Copyright (C) 2010 Olafur Gauti Gudmundsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.code.morphia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.allanbank.mongodb.Durability;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

/**
 * 
 * @author Scott Hernandez
 */
public class TestDurability extends TestBase {

    @Entity(concern = "Safe")
    static class Simple {
        @Id
        String id;

        public Simple(String id) {
            this();
            this.id = id;
        }

        private Simple() {
        }
    }

    @Test
    public void testDuplicateInsertWDefaultDurability() throws Exception {
        boolean failed = false;
        AdvancedDatastore aDs = (AdvancedDatastore) ds;
        try {
            aDs.insert(new Simple("simple"), ds.getDefaultDurability());
            aDs.insert(new Simple("simple"), ds.getDefaultDurability());
        }
        catch (Exception e) {
            failed = true;
        }
        assertEquals(1L, ds.getCount(Simple.class));
        assertTrue("Duplicate Exception was not raised!", failed);
    }

    @Test
    public void testDuplicateInsertWSafeDurability() throws Exception {
        boolean failed = false;
        AdvancedDatastore aDs = (AdvancedDatastore) ds;
        try {
            aDs.insert(new Simple("simple"));
            aDs.insert(new Simple("simple"), Durability.ACK);
        }
        catch (Exception e) {
            failed = true;
        }
        assertEquals(1L, ds.getCount(Simple.class));
        assertTrue("Duplicate Exception was not raised!", failed);
    }

    @Test
    public void testDuplicateInsertWEntityDurability() throws Exception {
        boolean failed = false;
        AdvancedDatastore aDs = (AdvancedDatastore) ds;
        ds.setDefaultDurability(Durability.NONE);
        try {
            aDs.insert(new Simple("simple"));
            aDs.insert(new Simple("simple"));
        }
        catch (Exception e) {
            failed = true;
        }
        assertEquals(1L, ds.getCount(Simple.class));
        assertTrue("Duplicate Exception was not raised!", failed);
    }
}