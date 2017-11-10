package com.ldg.dbmodule.model

import com.ldg.wrapper.results.StatsTypes.{minuteGoal, playerName, teamName, urlLink}

/**
  *
  * formartting info
  *
  * Replacing name "'" X "''"
  *
  * Replacing location "'" X "''"
  *
  * Replacing goal String X Int
  *
  **/


object Teams {

  case class AwayTeam(name:teamName,goals:Int,goalsPlayer:Map[playerName,List[minuteGoal]])
  case class HomeTeam(name:teamName,goals:Int,goalsPlayer:Map[playerName,List[minuteGoal]])
  case class TeamPerSeason(season:String,teamName:teamName,url:urlLink)

}