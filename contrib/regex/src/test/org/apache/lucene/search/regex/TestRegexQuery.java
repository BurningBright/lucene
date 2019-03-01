package org.apache.lucene.search.regex;

/**
 * Copyright 2005 Apache Software Foundation
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

import junit.framework.TestCase;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.IndexSearcher;

import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;

public class TestRegexQuery extends TestCase {
  private IndexSearcher searcher;
  private final String FN = "field";

  public void setUp() {
    RAMDirectory directory = new RAMDirectory();
    try {
      IndexWriter writer = new IndexWriter(directory, new SimpleAnalyzer(), true);
      Document doc = new Document();
      doc.add(new Field(FN, "the quick brown fox jumps over the lazy dog", Field.Store.NO, Field.Index.TOKENIZED));
      writer.addDocument(doc);
      writer.optimize();
      writer.close();
      searcher = new IndexSearcher(directory);
    } catch (Exception e) {
      fail(e.toString());
    }
  }

  public void tearDown() {
    try {
      searcher.close();
    } catch (Exception e) {
      fail(e.toString());
    }
  }

  private Term newTerm(String value) { return new Term(FN, value); }

  private int  regexQueryNrHits(String regex) throws Exception {
    org.apache.lucene.search.regex.RegexQuery query = new org.apache.lucene.search.regex.RegexQuery( newTerm(regex));
    return searcher.search(query).length();
  }

  private int  spanRegexQueryNrHits(String regex1, String regex2, int slop, boolean ordered) throws Exception {
    org.apache.lucene.search.regex.SpanRegexQuery srq1 = new org.apache.lucene.search.regex.SpanRegexQuery( newTerm(regex1));
    org.apache.lucene.search.regex.SpanRegexQuery srq2 = new org.apache.lucene.search.regex.SpanRegexQuery( newTerm(regex2));
    SpanNearQuery query = new SpanNearQuery( new SpanQuery[]{srq1, srq2}, slop, ordered);
    return searcher.search(query).length();
  }

  public void testRegex1() throws Exception {
    assertEquals(1, regexQueryNrHits("^q.[aeiou]c.*$"));
  }

  public void testRegex2() throws Exception {
    assertEquals(0, regexQueryNrHits("^.[aeiou]c.*$"));
  }

  public void testRegex3() throws Exception {
    assertEquals(0, regexQueryNrHits("^q.[aeiou]c$"));
  }

  public void testSpanRegex1() throws Exception {
    assertEquals(1, spanRegexQueryNrHits("^q.[aeiou]c.*$", "dog", 6, true));
  }

  public void testSpanRegex2() throws Exception {
    assertEquals(0, spanRegexQueryNrHits("^q.[aeiou]c.*$", "dog", 5, true));
  }

  public void testEquals() throws Exception {
    org.apache.lucene.search.regex.RegexQuery query1 = new org.apache.lucene.search.regex.RegexQuery( newTerm("foo.*"));
    query1.setRegexImplementation(new org.apache.lucene.search.regex.JakartaRegexpCapabilities());

    org.apache.lucene.search.regex.RegexQuery query2 = new org.apache.lucene.search.regex.RegexQuery( newTerm("foo.*"));
    assertFalse(query1.equals(query2));
  }

}

