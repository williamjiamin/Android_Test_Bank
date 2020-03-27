package com.lexueoude.lxodtestbank;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.lexueoude.lxodtestbank.QuizContract.*;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "LXODTestBank.db";
    private static final int DATABASE_VERSION = 1;

    private static QuizDbHelper instance;

    private SQLiteDatabase db;

    private QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized QuizDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new QuizDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                CategoriesTable.TABLE_NAME + "( " +
                CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CategoriesTable.COLUMN_NAME + " TEXT " +
                ")";

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionTable.TABLE_NAME + " ( " +
                QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionTable.COLUMN_QUESTION + " TEXT, " +
                QuestionTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionTable.COLUMN_ANSWER_NUM + " INTEGER, " +
                QuestionTable.COLUMN_DIFFICULTY + " TEXT, " +
                QuestionTable.COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + QuestionTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
                CategoriesTable.TABLE_NAME + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +
                ")";

        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillCategoriesTable();
        fillQuestionTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CategoriesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuestionTable.TABLE_NAME);
        onCreate(db);
    }


    @TargetApi(16)
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void fillCategoriesTable(){
        Category c1 = new Category("Java");
        addCategory(c1);
        Category c2 = new Category("Android");
        addCategory(c2);
        Category c3 = new Category("Python");
        addCategory(c3);
    }


    private void addCategory(Category category) {
        ContentValues cv = new ContentValues();
        cv.put(CategoriesTable.COLUMN_NAME, category.getName());
        db.insert(CategoriesTable.TABLE_NAME, null, cv);
    }

    private void fillQuestionTable(){
        Question q1 =new Question("[Java类目的题目]（初级难度）这个是什么课程啊？",
                "A:Java的课程","B:Android课程啊老铁","C:PHP天下第一，我不管！",
                2,Question.DIFFICULTY_EASY,Category.JAVA);
        addQuestion(q1);
        Question q2 =new Question("[Android类目的题目]（初级难度）这个课程是什么机构出的啊？",
                "A:不知道","B:乐学偶得出品","C:偶得乐学",
                2,Question.DIFFICULTY_EASY,Category.ANDROID);
        addQuestion(q2);
        Question q3 =new Question("[Java类目的题目]（中级难度）Java学习的三大框架是啥",
                "A:SSM+SSH","B:AAA+BBB","C:CCC+DDD",
                1,Question.DIFFICULTY_MEDIUM,Category.JAVA);
        addQuestion(q3);
        Question q4 =new Question("[Python类目的题目]（中级难度）这个是什么课程呢？",
                "A:Java的课程","B:Android课程啊老铁","C:PHP天下第一，我不管！",
                2,Question.DIFFICULTY_MEDIUM,Category.PYTHON);
        addQuestion(q4);
        Question q5 =new Question("[Java类目的题目]（高级难度）答案B",
                "A:Java的课程","B:Android课程啊老铁","C:PHP天下第一，我不管！",
                2,Question.DIFFICULTY_HARD,Category.JAVA);
        addQuestion(q5);
        Question q6 =new Question("[Android类目的题目]（高级难度）答案B",
                "A:Java的课程","B:Android课程啊老铁","C:PHP天下第一，我不管！",
                2,Question.DIFFICULTY_HARD,Category.ANDROID);
        addQuestion(q6);
        Question q7 =new Question("[Android类目的题目]（中级难度）这个课程是什么机构出的啊？",
                "A:不知道","B:乐学偶得出品","C:偶得乐学",
                2,Question.DIFFICULTY_MEDIUM,Category.ANDROID);
        addQuestion(q7);

    }

    private void addQuestion(Question question){
        ContentValues cv = new ContentValues();
        cv.put(QuestionTable.COLUMN_QUESTION,question.getQuestion());
        cv.put(QuestionTable.COLUMN_OPTION1,question.getOption1());
        cv.put(QuestionTable.COLUMN_OPTION2,question.getOption2());
        cv.put(QuestionTable.COLUMN_OPTION3,question.getOption3());
        cv.put(QuestionTable.COLUMN_ANSWER_NUM,question.getAnswerNum());
        cv.put(QuestionTable.COLUMN_DIFFICULTY,question.getDifficulty());
        cv.put(QuestionTable.COLUMN_CATEGORY_ID, question.getCategoryID());
        db.insert(QuestionTable.TABLE_NAME,null,cv);
    }

    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + CategoriesTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(c.getInt(c.getColumnIndex(CategoriesTable._ID)));
                category.setName(c.getString(c.getColumnIndex(CategoriesTable.COLUMN_NAME)));
                categoryList.add(category);
            } while (c.moveToNext());
        }

        c.close();
        return categoryList;
    }

    public ArrayList<Question> getAllQuestion(){
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " +QuestionTable.TABLE_NAME,null);

        if (c.moveToFirst()){
            do{
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setAnswerNum(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NUM)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);

            }while(c.moveToNext());

        }

        c.close();
        return questionList;
    }


    public ArrayList<Question> getQuestions(int categoryID, String difficulty) {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();

        String selection = QuestionTable.COLUMN_CATEGORY_ID + " = ? " +
                " AND " + QuestionTable.COLUMN_DIFFICULTY + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(categoryID), difficulty};

        Cursor c = db.query(
                QuestionTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setAnswerNum(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NUM)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }
}
