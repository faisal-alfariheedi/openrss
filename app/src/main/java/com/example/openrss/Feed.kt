package com.example.openrss

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.io.Serializable


@Root(name = "feed",strict = false)
class Feed constructor() : Serializable {
    @field:Element(name = "icon",required = false)
    var icon: String? = null

    @field:Element(name = "id",required = false)
    var id: String? = null

    @field:Element(name = "logo",required = false)
    var logo: String? = null

    @field:Element(name = "title",required = false)
    var title: String? = null

    @field:Element(name = "updated",required = false)
    var updated: String? = null

    @field:Element(name = "subtitle",required = false)
    var subtitle: String? = null

    @field:ElementList(inline = true, name = "entry")
    var entrys: List<Entry>? = null


}