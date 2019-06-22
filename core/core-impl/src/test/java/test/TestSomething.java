package test;

import com.ycg.ksh.common.entity.Page;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.core.driver.application.DriverApplicationService;
import com.ycg.ksh.core.driver.application.dto.DriverAwaitInfoDto;
import com.ycg.ksh.core.driver.application.dto.DriverCarDto;
import com.ycg.ksh.core.driver.application.dto.DriverDto;
import com.ycg.ksh.core.driver.application.dto.DriverRouteDto;
import com.ycg.ksh.core.util.Constants;
import com.ycg.ksh.entity.service.PageScope;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@RunWith(SpringJUnit4ClassRunner.class)  
@ActiveProfiles("local")                                     
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class TestSomething {

	private static final Long dkey = 1161L;

	@Resource
	private DriverApplicationService driverApplicationService;

	@Before
	public void inti(){
		Globallys.initializeIDBuilder("192.168.0.100");
	}

	@Test
	public void register() throws ParameterException, BusinessException, Exception {
		int size = 10;

		Collection<DriverCarDto> carDtos = new ArrayList<>(size);
		Collection<DriverRouteDto> routeDtos = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			DriverCarDto carDto = new DriverCarDto();
			carDto.setType(i + 1);
			carDto.setLength(i + 4f);
			carDto.setLoadValue(i + 3f);
			carDto.setLicense("沪A1234"+  i);

			carDtos.add(carDto);

			DriverRouteDto routeDto = new DriverRouteDto();
			routeDto.setType(i + 1);
			routeDto.setStarts(new String[]{"上海", "上海市","嘉定区"});
			routeDto.setEnds(new String[]{"湖北省", "武汉市","武昌区"});

			routeDtos.add(routeDto);

		}
		DriverDto driverDto = new DriverDto();
		driverDto.setPhone("13625461358");
		driverDto.setName("测试司机");

		driverDto.setRoutes(routeDtos);
		driverDto.setCars(carDtos);

		driverApplicationService.register(1162, driverDto);
	}

	@Test
	public void modify() throws ParameterException, BusinessException, Exception {
		int size = 10;

		Collection<DriverCarDto> carDtos = new ArrayList<>(size);
		Collection<DriverRouteDto> routeDtos = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			DriverCarDto carDto = new DriverCarDto();
			carDto.setType(2 * i + 1);
			carDto.setLength(i + 4f);
			carDto.setLoadValue(i + 3f);
			carDto.setLicense("沪A1234"+  i);

			carDtos.add(carDto);

			DriverRouteDto routeDto = new DriverRouteDto();
			routeDto.setType(i + 1);
			routeDto.setStarts(new String[]{"北京", "北京市","朝阳区"});
			routeDto.setEnds(new String[]{"湖北省", "武汉市","武昌区"});

			routeDtos.add(routeDto);

		}
		DriverDto driverDto = new DriverDto();

		driverDto.setDriverKey(dkey);

		driverDto.setPhone("13625461358");
		driverDto.setName("测试司机");

		driverDto.setRoutes(routeDtos);
		driverDto.setCars(carDtos);

		driverApplicationService.modify(driverDto);

	}

	@Test
	public void get() throws ParameterException, BusinessException, Exception {
		DriverDto driverDto = driverApplicationService.getDriver(dkey);

		System.out.println("================================================================================================");
		System.out.println(driverDto);
		System.out.println("================================================================================================");
	}

	@Test
	public void release() throws ParameterException, BusinessException, Exception {

		DriverAwaitInfoDto awaitInfoDto = new DriverAwaitInfoDto();

		awaitInfoDto.setDriverKey(dkey);

		awaitInfoDto.setRouteType(Constants.ROUTE_TYPE_CT);
		awaitInfoDto.setStart("上海上海市嘉定区");
		awaitInfoDto.setEnd("北京北京市朝阳区");
		awaitInfoDto.setStartTime(LocalDate.now());

		awaitInfoDto.setCarType(Constants.CAR_TYPE_DL);
		awaitInfoDto.setLength(4.6F);
		awaitInfoDto.setLicense("沪A12345");
		awaitInfoDto.setLoadValue(8.0F);

		awaitInfoDto.setRouteType(Constants.ROUTE_TYPE_CT);
		awaitInfoDto.setStart("北京北京市朝阳区");
		awaitInfoDto.setEnd("湖北省武汉市武昌区");

		driverApplicationService.releaseWaitInfo(awaitInfoDto);
	}
	@Test
	public void search() throws ParameterException, BusinessException, Exception {

		Page<DriverAwaitInfoDto> page = driverApplicationService.searchAwaitInfoByDriver(dkey, LocalDateTime.now(), "北京北京市朝阳区",  new PageScope(1, 10));


		System.out.println("================================================================================================");
		System.out.println(page);
		System.out.println("================================================================================================");
	}

	@Test
	public void search2() throws ParameterException, BusinessException, Exception {

		Page<DriverAwaitInfoDto> page = driverApplicationService.searchAwaitInfoByCompany(111L, 1, 10F, "北京北京市朝阳区",  new PageScope(1, 10));


		System.out.println("================================================================================================");
		System.out.println(page);
		System.out.println("================================================================================================");
	}

}
