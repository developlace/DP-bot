package pl.aztk.dpbot.punishments;

public enum WarnPredefinedReason {

        SPAM(10, 15),
        OFFTOPIC(10, 15),
        BAD_LANGUAGE(10, 15),
        FLAME_WAR(20, 20),
        REFLINK(30, 30),
        TROLLING(30, 30),
        OTHER(5, 30),
        TEST(1, 0),
        CUSTOM(0, 0);

        protected int warnPoints;
        protected int daysExpirationDate;

        WarnPredefinedReason(int warnPoints, int daysExpirationTime){
                this.warnPoints = warnPoints;
                this.daysExpirationDate = daysExpirationTime;
        }

        public int getWarnPoints(){
        return this.warnPoints;
        }

        public int getDaysExpirationTime(){
        return this.daysExpirationDate;
        }

}
