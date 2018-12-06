CREATE TABLE `Gateway` (
  `gwid` bigint(20) NOT NULL AUTO_INCREMENT,
  `lastseen` datetime(6) DEFAULT NULL,
  `ttngatewayid` varchar(255) DEFAULT NULL,
  `userid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`gwid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `Notification` (
  `notificationid` bigint(20) NOT NULL AUTO_INCREMENT,
  `userid` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`notificationid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;