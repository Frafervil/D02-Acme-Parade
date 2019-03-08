
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class FloatToStringConverter implements Converter<domain.Float, String> {

	@Override
	public String convert(final domain.Float floatB) {
		String result;

		if (floatB == null)
			result = null;
		else
			result = String.valueOf(floatB.getId());
		return result;
	}

}
