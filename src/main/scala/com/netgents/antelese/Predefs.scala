package com.netgents.antelese

object Predefs {

  def ensureClose[A <: { def close() }, B](closeable: A)(block: A => B): B = {
    try {
      block(closeable)
    }
    finally {
      closeable.close
    }
  }

  implicit def anyRefToOption[A <: AnyRef](anyRef: A): Option[A] = if (anyRef == null) None else Some(anyRef)
}
