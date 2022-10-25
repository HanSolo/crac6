/*
 * Copyright (c) 2022 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.hansolo.crac6;

import jdk.crac.*;
//import org.crac.*;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;


public class Main implements Resource {
    private static final Map<Long, Boolean> cache = new HashMap<>();


    // ******************** Constructor ***************************************
    public Main() {
        final long start = System.nanoTime();
        for (int i = 1 ; i < 50_000 ; i++) {
            isPrime(i);
        }
        isPrime(25000);
        System.out.println("Time to first response: " + ((System.nanoTime() - start) / 1_000_000) + " ms");
    }


    // ******************** Methods *******************************************
    @Override public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
        System.out.println("beforeCheckpoint() called in Main");
    }

    @Override public void afterRestore(Context<? extends Resource> context) throws Exception {
        System.out.println("afterRestore() called in Main");
        final long start = System.nanoTime();
        isPrime(25000);
        System.out.println("Time to first response: " + ((System.nanoTime() - start) / 1_000_000) + " ms");
    }

    private boolean isPrime(final long number) {
        if (number < 1) { return false; }
        if (cache.containsKey(number)) { return cache.get(number); }
        boolean isPrime = true;
        for (long n = number ; n > 0 ; n--) {
            if (n != number && n != 1 && number % n == 0) {
                isPrime = false;
                break;
            }
        }
        cache.put(number, isPrime);
        return isPrime;
    }

    private boolean isEmpty(final Path path) throws IOException {
        if (null == path || !Files.exists(path)) { return false; }
        if (Files.isDirectory(path)) {
            try (Stream<Path> entries = Files.list(path)) {
                return !entries.findFirst().isPresent();
            }
        }
        return false;
    }


    public static void main(String[] args) {
        long currentTime = System.currentTimeMillis();
        long vmStartTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        System.out.println("JVM startup time      : " + (currentTime - vmStartTime) + "ms");
        System.out.println("PID                   : " + ProcessHandle.current().pid());
        new Main();

        try {
            while (true) { Thread.sleep(1_000); }
        } catch (InterruptedException e) { }
    }
}