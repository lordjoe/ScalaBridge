package com.lordjoe.scala
/**
 * com.lordjoe.scala.Appendable 
 * User: Steve
 * Date: 7/13/2015
 */
trait CanAppend {
  // better to override  toAppendable
  def append(out: Appendable) {
   // val convert: Array[Byte] = UnicodeConvert.convert(toAppendable.getBytes("UTF-16"), "UTF-8")
    out.append(toAppendable)
      
  }
  // override if toString is inappropriate
  def toAppendable  : String = toString
}
