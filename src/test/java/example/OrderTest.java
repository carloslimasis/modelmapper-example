package example;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import example.dto.OrderDTO;

public class OrderTest {

	@Mock
	Order order;
	@Mock
	Address address;
	@Mock
	Customer customer;
	@Mock
	Name name;

	private ModelMapper modelMapper;

	@Before
	public void setUp() {
		modelMapper = new ModelMapper();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testOrderMapper() {

		Mockito.when(address.getCity()).thenReturn("Florianópolis");
		Mockito.when(address.getStreet()).thenReturn("SC - 401");

		Mockito.when(name.getFirstName()).thenReturn("José");
		Mockito.when(name.getLastName()).thenReturn("Aldo");

		Mockito.when(customer.getName()).thenReturn(name);

		Mockito.when(order.getBillingAddress()).thenReturn(address);
		Mockito.when(order.getCustomer()).thenReturn(customer);

		modelMapper.addMappings(new PropertyMap<Order, OrderDTO>() {
			protected void configure() {
				map().setBillingCity(source.getBillingAddress().getCity());
				map().setBillingStreet(source.getBillingAddress().getStreet());
				map().setCustomerFirstName(source.getCustomer().getName().getFirstName());
				map().setCustomerLastName(source.getCustomer().getName().getLastName());
			}
		});

		OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);

		assertEquals(order.getCustomer().getName().getFirstName(), orderDTO.getCustomerFirstName());
		assertEquals(order.getCustomer().getName().getLastName(), orderDTO.getCustomerLastName());
		assertEquals(order.getBillingAddress().getStreet(), orderDTO.getBillingStreet());
		assertEquals(order.getBillingAddress().getCity(), orderDTO.getBillingCity());
	}

}
