package com.example.openrss

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import java.io.Serializable

@Root(name = "entry", strict = false)
class Entry @JvmOverloads constructor(
    @field:Element(name = "content")
    @param:Element(name = "content")
    var content: String? = null,

    @field:Element(required = false, name = "author")
    @param:Element(name = "author")
    var author: Author? = null,

    @field:Element(name = "id")
    @param:Element(name = "id")
    var id: String? = null,

    @field:Element(name = "thumbnail", required = false)
    @param:Element(name="thumbnail")
    var  thumbnail: Thumbnail? =null,



    @field:Element(name = "title")
    @param:Element(name = "title")
    var title: String? = null,

    @field:Element(name = "updated")
    @param:Element(name = "updated")
    var updated: String? = null

) : Serializable {
    var thumb:String? = null


}
@Root(name = "thumbnail", strict = false)
 class Thumbnail constructor(
    @field:Attribute(name = "url",required=false)
    @param:Attribute(name ="url")
    var url: String? = null,
 ): Serializable {

}

@Root(name = "author", strict = false)
class Author constructor(
    @field:Element(name = "name")
    @param:Element(name ="name")
    var name: String? = null,

    @field:Element(name = "uri")
    @param:Element(name ="uri")
    var uri: String? = null
    ): Serializable {



    override fun toString(): String {
        return "Author{" + "name='" + name + '\'' + ", uri='" + uri + '\'' + '}'
    }
}