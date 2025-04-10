package hcmute.edu.vn.callandsms.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(
        entities = {
                CallEntity.class,
                SmsEntity.class,
                BlacklistEntity.class,
                ConversationEntity.class    // 👈 THÊM DÒNG NÀY
        },
        version = 8,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    public abstract ConversationDao conversationDao();

    public abstract CallDao callDao();
    public abstract SmsDao smsDao();
    public abstract BlacklistDao blacklistDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "app_database"
            ).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}