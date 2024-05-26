import com.easyJava.RunApplication;
import com.easyJava.entity.po.ProductInfo;
import com.easyJava.entity.query.ProductInfoQuery;
import com.easyJava.mappers.ProductInfoMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest(classes = RunApplication.class)
@RunWith(SpringRunner.class)
public class MapperTest {

    @Resource
    private ProductInfoMapper<ProductInfo, ProductInfoQuery> productInfoMapper;

    @Test
    public void selectList(){
        ProductInfoQuery query = new ProductInfoQuery();
        query.setCodeFuzzy("Oppo");
        List<ProductInfo> productInfos = productInfoMapper.selectList(query);
        System.out.println();
        for (ProductInfo productInfo:productInfos){
            System.out.println(productInfo);
        }

        Integer count = productInfoMapper.selectCount(query);
        System.out.println(count);
    }

    @Test
    public void insertTest(){
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCompanyId("COMP007");
        productInfo.setProductName("产品名称11");
        productInfo.setPrice(BigDecimal.valueOf(24100.00));
        productInfo.setSkuType(6);
        productInfo.setColorType(1);
        productInfo.setStock(Long.parseLong("421800"));
        productInfo.setStatus(0);
        productInfo.setCreateTime(new Date());
        productInfo.setCreateDate(new Date());
        System.out.println(productInfoMapper.insert(productInfo));
    }

    @Test
    public void insertOrUpdateTest(){
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCode("Oppo14");
        productInfo.setStock(Long.parseLong("527699"));

        productInfo.setCreateTime(new Date());
        productInfo.setCreateDate(new Date());
        System.out.println(productInfoMapper.insertOrUpdate(productInfo));
    }

    @Test
    public void insertBatch(){
        List<ProductInfo> productInfos = new ArrayList<>();
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCode("100");
        productInfo.setCreateTime(new Date());
        productInfo.setProductName("AI手机01");
        productInfos.add(productInfo);

        ProductInfo productInfo1 = new ProductInfo();
        productInfo1.setCode("200");
        productInfo1.setCreateTime(new Date());
        productInfo1.setProductName("AI手机02");
        productInfos.add(productInfo1);

        System.out.println(productInfoMapper.insertBatch(productInfos));
    }

    @Test
    public void insertOrUpdateBatch(){
        List<ProductInfo> productInfos = new ArrayList<>();
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCode("100");
        productInfo.setCompanyId("COMP008");
        productInfo.setCreateTime(new Date());
        productInfo.setProductName("AI手机01");
        productInfo.setPrice(BigDecimal.valueOf(43010));
        productInfo.setSkuType(7);
        productInfo.setColorType(1);
        productInfos.add(productInfo);

        ProductInfo productInfo1 = new ProductInfo();
        productInfo1.setCode("200");
        productInfo1.setCompanyId("COMP008");
        productInfo1.setCreateTime(new Date());
        productInfo1.setProductName("AI手机02");
        productInfo1.setPrice(BigDecimal.valueOf(43020));
        productInfo1.setSkuType(7);
        productInfo1.setColorType(2);
        productInfos.add(productInfo1);

        System.out.println(productInfoMapper.insertOrUpdateBatch(productInfos));
    }

    @Test
    public void selectByKey(){
        ProductInfo productInfo1 = productInfoMapper.selectById(36);
        ProductInfo productInfo2  = productInfoMapper.selectByCode("100");
        ProductInfo productInfo3  = productInfoMapper.selectBySkuTypeAndColorType(7,1);
        System.out.println(productInfo1);
        System.out.println(productInfo2);
        System.out.println(productInfo3);
    }

    @Test
    public void updateByKey(){
        ProductInfo productInfo1 = productInfoMapper.selectById(13);
        ProductInfo productInfo2  = productInfoMapper.selectById(14);
        ProductInfo productInfo3  = productInfoMapper.selectById(15);

        productInfo1.setProductName("小米1");
        productInfo2.setProductName("小米2");
        productInfo3.setProductName("小米3");
        productInfoMapper.updateById(productInfo1, 13);
        productInfoMapper.updateByCode(productInfo2, "CODE124");
        productInfoMapper.updateBySkuTypeAndColorType(productInfo3, 2,1);
    }


    @Test
    public void deleteByKey(){
        productInfoMapper.deleteById(19);
        productInfoMapper.deleteByCode("Oppo12");
        productInfoMapper.deleteBySkuTypeAndColorType(5,1);
    }


}
