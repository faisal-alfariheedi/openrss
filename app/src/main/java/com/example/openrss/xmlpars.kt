package com.example.openrss

import android.util.Log
import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

private val ns: String? = null

class xmlpars {
    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream): List<*> {
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readFeed(parser)
        }
    }
    @Throws(XmlPullParserException::class, IOException::class)
    private fun readFeed(parser: XmlPullParser): List<Entry> {
        val entries = mutableListOf<Entry>()
        parser.require(XmlPullParser.START_TAG, ns, "feed")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            // Starts by looking for the entry tag
            if (parser.name == "entry") {
                entries.add(readEntry(parser))
            } else {
                skip(parser)
            }
        }
        return entries
    }
    var author: String = ""
    data class Entry(val title: String, val category: ArrayList<String>, val author: String, val published: String, val updated: String, val summary: String)
//    data class Entry(val title: String, val author: String, val published: String, val updated: String, val summary: String)
    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
// to their respective "read" methods for processing. Otherwise, skips the tag.
    @Throws(XmlPullParserException::class, IOException::class)
    private fun readEntry(parser: XmlPullParser): Entry {
        parser.require(XmlPullParser.START_TAG, ns, "entry")
        var title: String = ""
        var category: ArrayList<String>? =null
        var summary: String = ""

        var pub: String = ""
        var upd: String = ""
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "title" -> title = readTitle(parser)
                "category"-> category = readcate(parser)
                "summary" -> summary = readSummary(parser)
                "author"-> author = readauth(parser)
                "published"-> pub = readpub(parser)
                "updated"-> upd = readupd(parser)
                else -> skip(parser)
            }
        }
    Log.d("catedata", "$category")
        return Entry(title, category!!,"author: $author", pub.removeRange(10,20), upd.removeRange(10,20), summary)
//        return Entry(title, "author: $author", pub.removeRange(10,20), upd.removeRange(10,20), summary)
    }

    // Processes title tags in the feed.
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readTitle(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "title")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "title")
        return title
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readauth(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "author")
        parser.nextTag()
        parser.require(XmlPullParser.START_TAG, ns, "name")
        author = readText(parser)

        if (author == null)author=""
        parser.nextTag()
        skip(parser)
        parser.nextTag()
        parser.require(XmlPullParser.END_TAG, ns, "author")
        return author
    }
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readupd(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "updated")
        val update = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "updated")
        return update
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readpub(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "published")
        val published = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "published")
        return published
    }

    // Processes summary tags in the feed.
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readSummary(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "summary")
        val summary = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "summary")
        return summary
    }
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readcate(parser: XmlPullParser): ArrayList<String> {
        var cat=arrayListOf<String>()
        parser.require(XmlPullParser.START_TAG, ns, "category")
        while (parser.name!="author") {
            cat.add(parser.getAttributeValue(null,"term"))
            parser.next()
            parser.nextTag()

        }
        readauth(parser)
//        parser.require(XmlPullParser.END_TAG, ns, "category")
        return cat
    }

    // For the tags title and summary, extracts their text values.
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }
    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
//        if(parser.name.equals("author"))depth=0
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}