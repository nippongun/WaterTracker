# WaterTracker
SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
String format = simpleDateFormat.format(new Date());
