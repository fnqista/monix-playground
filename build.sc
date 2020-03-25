import mill._, scalalib._

object polling extends SbtModule {
  def scalaVersion = "2.12.11"

  def ivyDeps = Agg(
    ivy"io.monix::monix:3.1.0"
  )
}


