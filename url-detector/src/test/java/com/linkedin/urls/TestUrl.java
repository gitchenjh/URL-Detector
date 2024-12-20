/**
 * Copyright 2015 LinkedIn Corp. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */
package com.linkedin.urls;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;


class TestUrl {

  @ParameterizedTest
  @CsvSource({
    "http://www.google.com/, www.google.com, /, '', ''",
    "nooooo:password@teeee.com, teeee.com, /, nooooo, password",
    "hello:ono@bob.com/lala.html, bob.com, /lala.html, hello, ono",
    "lala:asdfjdj1k@bob.com, bob.com, /, lala, asdfjdj1k",
    "sdf@bob.com, bob.com, /, sdf, ''",
    "@www.google.com, www.google.com, /, '', ''",
    "lalal:@www.gogo.com, www.gogo.com, /, lalal, ''",
    "nono:boo@[::1], [::1], /, nono, boo",
    "nono:boo@yahoo.com/@1234, yahoo.com, /@1234, nono, boo",
    "big.big.boss@google.com, google.com, /, big.big.boss, ''",
    "big.big.boss@013.xxx, 013.xxx, /, big.big.boss, ''",
    "big.big.boss@0xb02067cz, 0xb02067cz, /, big.big.boss, ''",
  })
  void testUsernamePasswordUrls(String testInput, String host, String path, String username, String password)
      throws MalformedURLException {
    Url url = Url.create(testInput);
    assertEquals(url.getHost(), host);
    assertEquals(url.getPath(), path);
    assertEquals(url.getUsername(), username);
    assertEquals(url.getPassword(), password);
  }

  @ParameterizedTest
  @CsvSource({
    "http://www.google.com:820, www.google.com, /, 820",
    "foooo.coo:80, foooo.coo, /, 80",
    "[::ffff:192.168.1.1]:800, [::ffff:192.168.1.1], /, 800",
    "[::1]:900/dodododo, [::1], /dodododo, 900",
    "hdh:@[::1]:9/nono, [::1], /nono, 9",
    "http://touch.www.linkedin.com:9000, touch.www.linkedin.com, /, 9000"
  })
  void testPort(String testInput, String host, String path, int port) throws MalformedURLException {
    Url url = Url.create(testInput);
    assertEquals(url.getHost(), host);
    assertEquals(url.getPath(), path);
    assertEquals(url.getPort(), port);
  }

  @ParameterizedTest
  @CsvSource({
    "http://www.google.com/, www.google.com, /, ''",
    "www.google.com/lala?here=2, www.google.com, /lala, ?here=2",
    "bewp.bop.com/boop?bip=2&bep=3, bewp.bop.com, /boop, ?bip=2&bep=3",
    "[fe80::1:192.168.12.3]/nooo?dop=2&wop=4, [fe80::1:192.168.12.3], /nooo, ?dop=2&wop=4",
    "[::1:192.1.1.1]:80/nooo?dop=[::1]&wop=4, [::1:192.1.1.1], /nooo, ?dop=[::1]&wop=4"
  })
  void testQuery(String testInput, String host, String path, String query) throws MalformedURLException {
    Url url = Url.create(testInput);
    assertEquals(url.getHost(), host);
    assertEquals(url.getPath(), path);
    assertEquals(url.getQuery(), query);
  }

  @ParameterizedTest
  @CsvSource({
    "http://www.google.com/, http, www.google.com, /",
    "//www.google.com/, '', www.google.com, /",
    "//123825342/, '', 123825342, /",
    "//hello/, '', hello, /",
    "//hello:/, '', hello, /"
  })
  void testScheme(String testInput, String scheme, String host, String path) throws MalformedURLException {
    Url url = Url.create(testInput);
    assertEquals(url.getScheme(), scheme);
    assertEquals(url.getHost(), host);
    assertEquals(url.getPath(), path);
  }

  @ParameterizedTest
  @CsvSource({
    "www.booopp.com:20#fa, www.booopp.com, http://www.booopp.com:20/#fa",
    "www.yahooo.com:20?fff#aa, www.yahooo.com, http://www.yahooo.com:20/?fff#aa",
    "www.google.com#fa, www.google.com, http://www.google.com/#fa",
    "www.google.com?3fd#fa, www.google.com, http://www.google.com/?3fd#fa",
    "//www.google.com/, www.google.com, //www.google.com/",
    "http://www.google.com/, www.google.com, http://www.google.com/",
    "ftp://whosdere:me@google.com/, google.com, ftp://whosdere:me@google.com/",
    "ono:doope@fb.net:9090/dhdh, fb.net, http://ono:doope@fb.net:9090/dhdh",
    "ono:a@fboo.com:90/dhdh/@1234, fboo.com, http://ono:a@fboo.com:90/dhdh/@1234",
    "fbeoo.net:990/dhdeh/@1234, fbeoo.net, http://fbeoo.net:990/dhdeh/@1234",
    "fbeoo:@boop.com/dhdeh/@1234?aj=r, boop.com, http://fbeoo@boop.com/dhdeh/@1234?aj=r",
    "bloop:@noooo.com/doop/@1234, noooo.com, http://bloop@noooo.com/doop/@1234",
    "bah.com/lala/@1234/@dfd@df?@dsf#ono, bah.com, http://bah.com/lala/@1234/@dfd@df?@dsf#ono",
    "https://dewd:dood@www.google.com:20/?why=is&this=test#?@Sdsf, www.google.com, https://dewd:dood@www.google.com:20/?why=is&this=test#?@Sdsf",
    "http://013.xxx/, 013.xxx, http://013.xxx/"
  })
  void testHostAndFullUrl(String testInput, String host, String fullUrl) throws MalformedURLException
  {
    Url url = Url.create(testInput);
    assertEquals(url.getHost(), host, testInput);
    assertEquals(url.getFullUrl(), fullUrl);
    int fragmentIndex = fullUrl.indexOf("#");
    assertEquals(url.getFullUrlWithoutFragment(),
      fragmentIndex == -1 ? fullUrl : fullUrl.substring(0, fragmentIndex));
  }

  @ParameterizedTest
  @CsvSource({
    "localhost:9000/, localhost, 9000, http://localhost:9000/",
    "go/tj, go, 80, http://go/tj"
  })
  void testSingleDomainUrls(String testInput, String host, int port, String fullUrl)
      throws MalformedURLException {
    Url url = Url.create(testInput);
    assertEquals(url.getHost(), host);
    assertEquals(url.getPort(), port);
    assertEquals(url.getFullUrl(), fullUrl);
  }

  static Stream<Arguments> getUrlsForSchemeDetectionBySuffix() {
    String domain = "linkedin.com";
    return Stream.of("http://", "https://", "ftp://", "ftps://", "http%3a//", "https%3a//", "ftp%3a//", "ftps%3a//")
      .map(validScheme -> new Object[][]{
        {validScheme + domain, validScheme},
        {validScheme.toUpperCase() + domain, validScheme.toUpperCase()},
        {"sometext" + validScheme + domain, validScheme },
        {"sometext" + validScheme.toUpperCase() + domain, validScheme.toUpperCase()}
      })
      .flatMap(Arrays::stream)
      .map(Arguments::of);
  }

  @ParameterizedTest
  @MethodSource("getUrlsForSchemeDetectionBySuffix")
  void testSchemeDetectionBySuffix(String text, String expected) throws MalformedURLException {
    Url url = Url.create(text);
    assertEquals(url.getScheme(), expected.replace("://",""));
  }
}
