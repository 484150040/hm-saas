package com.hm.digital.twin.biz.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.hm.digital.inface.biz.RedisService;


/**
 * redis操作Service的实现类
 * Created by macro on 2018/8/7.
 */
@Service
public class RedisServiceimpl implements RedisService {
  @Autowired
  private StringRedisTemplate stringRedisTemplate;

  @Override
  public void set(String key, String value) {
    stringRedisTemplate.opsForValue().set(key, value);
  }

  @Override
  public String get(String key) {
    return stringRedisTemplate.opsForValue().get(key);
  }

  @Override
  public boolean expire(String key, long expire) {
    return stringRedisTemplate.expire(key, expire, TimeUnit.SECONDS);
  }

  @Override
  public void remove(String key) {
    stringRedisTemplate.delete(key);
  }

  @Override
  public Long increment(String key, long delta) {
    return stringRedisTemplate.opsForValue().increment(key,delta);
  }
  @Override
  public boolean set(String key, String value, long time) {
    try {
      if (time > 0) {
        stringRedisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
      } else {
        set(key, value);
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

  }

  @Override
  public Object hget(String key, String item) {
    return stringRedisTemplate.opsForHash().get(key, item);
  }

  @Override
  public Map<Object, Object> hmget(String key) {
    return stringRedisTemplate.opsForHash().entries(key);
  }

  @Override
  public boolean hmset(String key, Map<String, Object> map) {
    try {
      stringRedisTemplate.opsForHash().putAll(key, map);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean hmset(String key, Map<String, Object> map, long time) {
    try {
      stringRedisTemplate.opsForHash().putAll(key, map);
      if (time > 0) {
        expire(key, time);
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean hset(String key, String item, Object value) {
    try {
      stringRedisTemplate.opsForHash().put(key, item, value);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean hset(String key, String item, Object value, long time) {
    try {
      stringRedisTemplate.opsForHash().put(key, item, value);
      if (time > 0) {
        expire(key, time);
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public void hdel(String key, Object... item) {
    stringRedisTemplate.opsForHash().delete(key, item);
  }

  @Override
  public boolean hHasKey(String key, String item) {
    return stringRedisTemplate.opsForHash().hasKey(key, item);
  }

  @Override
  public double hincr(String key, String item, double by) {
    return stringRedisTemplate.opsForHash().increment(key, item, by);
  }

  @Override
  public double hdecr(String key, String item, double by) {
    return stringRedisTemplate.opsForHash().increment(key, item, -by);
  }

  @Override
  public Set<String> sGet(String key) {
    try {
      return stringRedisTemplate.opsForSet().members(key);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public boolean sHasKey(String key, Object value) {
    try {
      return stringRedisTemplate.opsForSet().isMember(key, value);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public long sSet(String key, String... values) {
    try {
      return stringRedisTemplate.opsForSet().add(key, values);
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  @Override
  public long sSetAndTime(String key, long time, String... values) {
    try {
      Long count = stringRedisTemplate.opsForSet().add(key, values);
      if (time > 0)
        expire(key, time);
      return count;
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  @Override
  public long sGetSetSize(String key) {
    try {
      return stringRedisTemplate.opsForSet().size(key);
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  @Override
  public long setRemove(String key, Object... values) {
    try {
      Long count = stringRedisTemplate.opsForSet().remove(key, values);
      return count;
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }
  @Override
  public List<String> lGet(String key, long start, long end) {
    try {
      return stringRedisTemplate.opsForList().range(key, start, end);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public long lGetListSize(String key) {
    try {
      return stringRedisTemplate.opsForList().size(key);
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

 @Override
  public Object lGetIndex(String key, long index) {
    try {
      return stringRedisTemplate.opsForList().index(key, index);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public boolean lSet(String key, String value) {
    try {
      stringRedisTemplate.opsForList().rightPush(key, value);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean lSet(String key, String value, long time) {
    try {
      stringRedisTemplate.opsForList().rightPush(key, value);
      if (time > 0)
        expire(key, time);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean lSet(String key, List<String> value) {
    try {
      stringRedisTemplate.opsForList().rightPushAll(key, value);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

 @Override
  public boolean lSet(String key, List<String> value, long time) {
    try {
      stringRedisTemplate.opsForList().rightPushAll(key, value);
      if (time > 0)
        expire(key, time);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean lUpdateIndex(String key, long index, String value) {
    try {
      stringRedisTemplate.opsForList().set(key, index, value);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public long lRemove(String key, long count, Object value) {
    try {
      Long remove = stringRedisTemplate.opsForList().remove(key, count, value);
      return remove;
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  
  
}