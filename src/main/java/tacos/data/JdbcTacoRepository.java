package tacos.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import tacos.Ingredient;
import tacos.Taco;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Repository
public class JdbcTacoRepository implements TacoRepository {

    private JdbcTemplate jdbcTemplate;

    public JdbcTacoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Taco save(Taco design) {
        long tacoId = saveTacoInfo(design);

        List<String> ingredients = design.getIngredients();
        for (String s:ingredients){
            saveIngredientToTaco(tacoId, s);
        }

        return design;
    }

    private long saveTacoInfo(Taco design) {
        design.setCreatedAt(new Date());

        PreparedStatementCreator psc = new PreparedStatementCreatorFactory("insert into " +
                "Taco(name, createdAt) values (?, ?)",
                Types.VARCHAR, Types.TIMESTAMP).newPreparedStatementCreator(Arrays.asList(
                design.getName(),
                new Timestamp(design.getCreatedAt().getTime())
        ));

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);
        //long v = keyHolder.getKey().longValue();
        return 1L;
    }

    private void saveIngredientToTaco(long tacoId, String ingredient_id){
        jdbcTemplate.update("insert into Taco_Ingredients(taco, ingredient)" +
                "values(?, ?)", tacoId, ingredient_id);
    }

}
