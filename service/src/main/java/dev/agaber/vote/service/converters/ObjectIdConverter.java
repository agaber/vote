package dev.agaber.vote.service.converters;

import com.google.common.base.Converter;
import org.bson.types.ObjectId;

import javax.annotation.Nullable;
import java.util.Optional;

public class ObjectIdConverter extends Converter<String, ObjectId> {
  private static final ObjectIdConverter INSTANCE = new ObjectIdConverter();

  public static ObjectIdConverter getInstance() {
    return INSTANCE;
  }

  @Nullable
  @Override
  protected ObjectId doForward(@Nullable String id) {
    return Optional.ofNullable(id).map(ObjectId::new).orElse(null);
  }

  @Nullable
  @Override
  protected String doBackward(@Nullable ObjectId objectId) {
    return Optional.ofNullable(objectId).map(ObjectId::toHexString).orElse(null);
  }
}
