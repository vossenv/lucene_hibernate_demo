package com.dm.teamquery.data;

import com.dm.teamquery.execption.ResourceCreationFailedException;
import lombok.Data;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.stream.Collectors;

import static com.dm.teamquery.config.LambdaExceptionUtil.rethrowFunction;


@Data

public class SearchResponse {
    private SearchRequest request;
    private long rowCount;
    private Double searchTime;
    private List<?> resultsList;


    public SearchResponse(SearchRequest request){
        this.request = request;
    }

    public ResponseEntity getResponse (Class type, Class dest) throws ResourceCreationFailedException{
        try {
            Constructor<?> c = dest.getConstructor(type);
            Resources r = new Resources(resultsList.stream()
                    .map(rethrowFunction(o -> c.newInstance(type.cast(o))))
                    .collect(Collectors.toList()));
            return request.prepareResponse(r, this);
        } catch (Exception e) {
            throw new ResourceCreationFailedException(ExceptionUtils.getRootCauseMessage(e));
        }
    }
}






//    static <T> Consumer<T> throwingConsumerWrapper(
//            ThrowingConsumer<T, Exception> throwingConsumer) {
//
//        return i -> {
//            try {
//                throwingConsumer.accept(i);
//            } catch (Exception ex) {
//                throw new RuntimeException(ex);
//            }
//        };
//    }

//@FunctionalInterface
//interface ThrowingConsumer<T, E extends Exception> {
//    void accept(T t) throws E;
//}

// List<Object> nl = new ArrayList<>();
//resultsList.forEach(throwingConsumerWrapper( (o) -> nl.add(constructor.newInstance(type.cast(o)))));
