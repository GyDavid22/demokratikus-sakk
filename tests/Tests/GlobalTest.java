package Tests;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import DataStructures.*;

@RunWith(Categories.class)
@Categories.IncludeCategory(GlobalTestsInterface.class)
@Suite.SuiteClasses({BoardTest.class, GameTest.class, PawnTest.class})
public class GlobalTest {
    // minden teszt lefut
}
