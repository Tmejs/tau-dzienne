#include "catch.hpp"

#include <rpn.hpp>

using Catch::Matchers::EndsWith; // or Catch::EndsWith

TEST_CASE("Some test") {
    std::string str = "testowanie aplikacji";
    REQUIRE_THAT( str, EndsWith( "aplikacji" ) ); 
}

TEST_CASE("Some test 2") {
    REQUIRE ( rpn_count("10 a24sd +") == 12 );
}

SCENARIO ("Simple scenario") {
    GIVEN ("there are numbers 10 and 20") {
        int x= 10, y = 20;
        WHEN ("we add them") {
            int z = x + y;
            THEN("the result should be 30"){
                REQUIRE(z == 30);
            }
        }
    }
}
