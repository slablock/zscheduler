package com.github.slablock.zcheduler.core.domain

trait Expression {

  def isValid: Boolean

  def getExpression: String

}
